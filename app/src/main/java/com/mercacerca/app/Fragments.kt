package com.mercacerca.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

// =====================  ADMINISTRADOR  =====================

class DashboardFragment : Fragment(R.layout.fragment_dashboard)

class SalesReportFragment : Fragment(R.layout.fragment_sales_report) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.btnExport).setOnClickListener {
            Toast.makeText(requireContext(), R.string.guardado_ok, Toast.LENGTH_SHORT).show()
        }
    }
}

/** Conecta un RecyclerView con su buscador y la acción de eliminar con confirmación. */
private fun <T> bindList(
    rv: RecyclerView,
    search: EditText,
    all: MutableList<T>,
    layout: Int,
    matches: (T, String) -> Boolean,
    bind: (View, T, () -> Unit) -> Unit
) {
    lateinit var adapter: SimpleAdapter<T>
    fun refresh() {
        val q = search.text.toString().trim()
        adapter.setItems(all.filter { q.isEmpty() || matches(it, q) })
    }
    adapter = SimpleAdapter(all.toMutableList(), layout) { v, item, _ ->
        bind(v, item) { all.remove(item); refresh() }
    }
    rv.adapter = adapter
    search.doAfterTextChanged { refresh() }
}

/** Lista reutilizable: usuarios, productos (admin / vendedor) y pedidos. */
class CrudListFragment : Fragment(R.layout.fragment_crud_list) {

    companion object {
        const val USERS = "users"
        const val PRODUCTS_ADMIN = "products_admin"
        const val PRODUCTS_SELLER = "products_seller"
        const val ORDERS = "orders"

        fun newInstance(type: String) = CrudListFragment().apply {
            arguments = Bundle().apply { putString("type", type) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val type = arguments?.getString("type") ?: USERS
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val search = view.findViewById<EditText>(R.id.etSearch)
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val subtitle = view.findViewById<TextView>(R.id.tvSubtitle)
        rv.layoutManager = LinearLayoutManager(requireContext())

        when (type) {
            USERS -> {
                title.setText(R.string.gestion_usuarios)
                subtitle.setText(R.string.gestion_usuarios_sub)
                fab.setOnClickListener { startActivity(Intent(requireContext(), UserFormActivity::class.java)) }
                bindList(rv, search, DemoData.users.toMutableList(), R.layout.item_user,
                    { u, q -> u.name.contains(q, true) || u.email.contains(q, true) }
                ) { v, u, onDelete ->
                    v.findViewById<TextView>(R.id.tvAvatar).text = u.name.first().toString()
                    v.findViewById<TextView>(R.id.tvName).text = u.name
                    v.findViewById<TextView>(R.id.tvEmail).text = u.email
                    v.findViewById<TextView>(R.id.tvRole).text = u.role
                    v.findViewById<View>(R.id.btnEdit).setOnClickListener {
                        startActivity(
                            Intent(requireContext(), UserFormActivity::class.java)
                                .putExtra("name", u.name).putExtra("email", u.email).putExtra("role", u.role)
                        )
                    }
                    v.findViewById<View>(R.id.btnDelete).setOnClickListener {
                        confirmDelete(requireContext(), onDelete)
                    }
                }
            }

            PRODUCTS_ADMIN, PRODUCTS_SELLER -> {
                if (type == PRODUCTS_ADMIN) {
                    title.setText(R.string.gestion_productos)
                    subtitle.setText(R.string.gestion_productos_sub)
                } else {
                    title.setText(R.string.mis_productos)
                    subtitle.setText(R.string.panel_productos_sub)
                }
                fab.setOnClickListener { startActivity(Intent(requireContext(), ProductFormActivity::class.java)) }
                val source = if (type == PRODUCTS_ADMIN) DemoData.products
                else DemoData.products.filter { it.seller == "Finca La Esperanza" }
                bindList(rv, search, source.toMutableList(), R.layout.item_product_manage,
                    { p, q -> p.name.contains(q, true) || p.category.contains(q, true) }
                ) { v, p, onDelete ->
                    v.findViewById<TextView>(R.id.tvName).text = p.name
                    v.findViewById<TextView>(R.id.tvMeta).text = "${p.category} · ${p.stock} uds · ${p.seller}"
                    v.findViewById<TextView>(R.id.tvPrice).text = p.price.cop()
                    v.findViewById<View>(R.id.btnEdit).setOnClickListener {
                        startActivity(
                            Intent(requireContext(), ProductFormActivity::class.java)
                                .putExtra("name", p.name).putExtra("category", p.category)
                                .putExtra("price", p.price).putExtra("stock", p.stock)
                        )
                    }
                    v.findViewById<View>(R.id.btnDelete).setOnClickListener {
                        confirmDelete(requireContext(), onDelete)
                    }
                }
            }

            else -> { // ORDERS
                title.setText(R.string.historial_pedidos)
                subtitle.setText(R.string.historial_pedidos_sub)
                fab.visibility = View.GONE
                bindList(rv, search, DemoData.orders.toMutableList(), R.layout.item_order,
                    { o, q -> o.code.contains(q, true) || o.buyer.contains(q, true) }
                ) { v, o, _ ->
                    v.findViewById<TextView>(R.id.tvCode).text = o.code
                    val status = v.findViewById<TextView>(R.id.tvStatus)
                    status.text = o.status
                    status.setTextColor(
                        androidx.core.content.ContextCompat.getColor(
                            requireContext(), if (o.status == "Pendiente") R.color.accent_dark else R.color.success
                        )
                    )
                    v.findViewById<TextView>(R.id.tvBuyer).text = o.buyer
                    v.findViewById<TextView>(R.id.tvItems).text = o.items
                    v.findViewById<TextView>(R.id.tvTotal).text = o.total.cop()
                }
            }
        }
    }
}

// =====================  COMPRADOR  =====================

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rvFeatured)
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.isNestedScrollingEnabled = false
        rv.adapter = SimpleAdapter(DemoData.products.take(5).toMutableList(), R.layout.item_featured) { v, p, _ ->
            bindProductCard(v, p)
        }
    }
}

class CatalogFragment : Fragment(R.layout.fragment_catalog) {
    private var category = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = SimpleAdapter(DemoData.products.toMutableList(), R.layout.item_product) { v, p, _ ->
            bindProductCard(v, p)
        }
        rv.adapter = adapter

        val search = view.findViewById<EditText>(R.id.etSearch)
        val group = view.findViewById<ChipGroup>(R.id.chipGroup)

        fun refresh() {
            val q = search.text.toString().trim()
            adapter.setItems(DemoData.products.filter {
                (category.isEmpty() || it.category == category) && (q.isEmpty() || it.name.contains(q, true))
            })
        }
        search.doAfterTextChanged { refresh() }
        group.setOnCheckedStateChangeListener { g, ids ->
            category = if (ids.isEmpty() || ids[0] == R.id.chipAll) ""
            else g.findViewById<Chip>(ids[0]).text.toString()
            refresh()
        }
    }
}

class CartFragment : Fragment(R.layout.fragment_cart) {
    private var total = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val tvSubtotal = view.findViewById<TextView>(R.id.tvSubtotal)
        val tvShipping = view.findViewById<TextView>(R.id.tvShipping)
        val tvTotal = view.findViewById<TextView>(R.id.tvTotal)

        fun updateTotals() {
            val subtotal = DemoData.cart.sumOf { it.price * it.qty }
            val shipping = if (subtotal == 0 || subtotal >= 50000) 0 else 5000
            total = subtotal + shipping
            tvSubtotal.text = subtotal.cop()
            tvShipping.text = shipping.cop()
            tvTotal.text = total.cop()
        }

        lateinit var adapter: SimpleAdapter<CartItem>
        adapter = SimpleAdapter(DemoData.cart, R.layout.item_cart) { v, item, _ ->
            v.findViewById<TextView>(R.id.tvName).text = item.name
            v.findViewById<TextView>(R.id.tvPrice).text = item.price.cop()
            v.findViewById<TextView>(R.id.tvQty).text = item.qty.toString()
            v.findViewById<View>(R.id.btnPlus).setOnClickListener {
                item.qty++; adapter.notifyDataSetChanged(); updateTotals()
            }
            v.findViewById<View>(R.id.btnMinus).setOnClickListener {
                if (item.qty > 1) item.qty--
                adapter.notifyDataSetChanged(); updateTotals()
            }
            v.findViewById<View>(R.id.btnRemove).setOnClickListener {
                DemoData.cart.remove(item); adapter.notifyDataSetChanged(); updateTotals()
            }
        }
        rv.adapter = adapter
        updateTotals()

        view.findViewById<View>(R.id.btnCheckout).setOnClickListener {
            startActivity(Intent(requireContext(), PaymentActivity::class.java).putExtra("total", total))
        }
    }
}

// =====================  PERFIL (vendedor / comprador)  =====================

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        fun newInstance(role: String) = ProfileFragment().apply {
            arguments = Bundle().apply { putString("role", role) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val (name, email, badge) = when (arguments?.getString("role")) {
            "admin" -> Triple("Ana Martínez", "ana@mercacerca.co", getString(R.string.rol_admin))
            "seller" -> Triple("Finca La Esperanza", "esperanza@correo.com", getString(R.string.rol_vendedor))
            else -> Triple("Laura Gómez", "laura@correo.com", getString(R.string.rol_comprador))
        }
        view.findViewById<TextView>(R.id.tvAvatar).text = name.first().toString()
        view.findViewById<TextView>(R.id.tvUserName).text = name
        view.findViewById<TextView>(R.id.tvRoleBadge).text = badge
        view.findViewById<EditText>(R.id.etName).setText(name)
        view.findViewById<EditText>(R.id.etEmail).setText(email)

        view.findViewById<View>(R.id.btnSaveProfile).setOnClickListener {
            Toast.makeText(requireContext(), R.string.guardado_ok, Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            (requireActivity() as AppCompatActivity).logout()
        }
    }
}
