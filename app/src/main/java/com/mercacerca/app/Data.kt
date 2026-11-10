package com.mercacerca.app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.util.Locale

// ---------- Modelos (datos de demostración: el frontend aún no consume API) ----------
data class AppUser(val name: String, val email: String, val role: String)
data class Product(val name: String, val category: String, val price: Int, val stock: Int, val seller: String)
data class Order(val code: String, val buyer: String, val items: String, val total: Int, val status: String)
data class CartItem(val name: String, val price: Int, var qty: Int)

object DemoData {
    val users = mutableListOf(
        AppUser("Laura Gómez", "laura@correo.com", "Comprador"),
        AppUser("Finca La Esperanza", "esperanza@correo.com", "Vendedor"),
        AppUser("Carlos Ruiz", "carlos@correo.com", "Comprador"),
        AppUser("Lácteos del Valle", "valle@correo.com", "Vendedor"),
        AppUser("Ana Martínez", "ana@mercacerca.co", "Administrador")
    )
    val products = mutableListOf(
        Product("Café de Huila 500 g", "Café", 28000, 40, "Finca La Esperanza"),
        Product("Queso campesino 500 g", "Lácteos", 16000, 25, "Lácteos del Valle"),
        Product("Aguacate Hass x3", "Frutas", 9000, 60, "Finca La Esperanza"),
        Product("Panela orgánica 1 kg", "Despensa", 7500, 80, "Trapiche San José"),
        Product("Miel de abeja 350 g", "Despensa", 22000, 30, "Apiario Sol"),
        Product("Chocolate de mesa 500 g", "Dulces", 11000, 50, "Cacao del Sur"),
        Product("Mermelada de guayaba", "Dulces", 12000, 35, "Dulces Doña Rosa"),
        Product("Yogur natural 1 L", "Lácteos", 8500, 45, "Lácteos del Valle")
    )
    val orders = mutableListOf(
        Order("PED-1042", "Laura Gómez", "2 x Café de Huila, 1 x Panela", 63500, "Pendiente"),
        Order("PED-1041", "Carlos Ruiz", "1 x Queso campesino", 21000, "Enviado"),
        Order("PED-1039", "Ana Martínez", "3 x Miel de abeja", 71000, "Entregado"),
        Order("PED-1036", "Juan Pérez", "2 x Mermelada de guayaba", 29000, "Entregado")
    )
    val cart = mutableListOf(
        CartItem("Café de Huila 500 g", 28000, 1),
        CartItem("Aguacate Hass x3", 9000, 2)
    )

    fun addToCart(name: String, price: Int) {
        val existing = cart.find { it.name == name }
        if (existing != null) existing.qty++ else cart.add(CartItem(name, price, 1))
    }
}

// ---------- Utilidades ----------
fun Int.cop(): String = "$" + NumberFormat.getInstance(Locale("es", "CO")).format(this)

fun confirmDelete(ctx: Context, onConfirm: () -> Unit) {
    MaterialAlertDialogBuilder(ctx)
        .setTitle(R.string.eliminar)
        .setMessage(R.string.confirmar_eliminar)
        .setPositiveButton(R.string.eliminar) { _, _ -> onConfirm() }
        .setNegativeButton(R.string.cancelar, null)
        .show()
}

fun AppCompatActivity.showFragment(fragment: Fragment, @StringRes title: Int) {
    supportActionBar?.setTitle(title)
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
}

fun AppCompatActivity.logout() {
    startActivity(
        Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    )
    finish()
}

/** Pinta una tarjeta de producto (catálogo y destacados) y conecta sus clics. */
fun bindProductCard(v: View, p: Product) {
    v.findViewById<TextView>(R.id.tvName).text = p.name
    v.findViewById<TextView>(R.id.tvCategory).text = p.category
    v.findViewById<TextView>(R.id.tvPrice).text = p.price.cop()
    v.setOnClickListener {
        v.context.startActivity(
            Intent(v.context, ProductDetailActivity::class.java)
                .putExtra("name", p.name).putExtra("category", p.category)
                .putExtra("price", p.price).putExtra("seller", p.seller)
        )
    }
    v.findViewById<View>(R.id.btnAdd).setOnClickListener {
        DemoData.addToCart(p.name, p.price)
        Toast.makeText(v.context, R.string.agregado_carrito, Toast.LENGTH_SHORT).show()
    }
}

// ---------- Adaptador genérico para RecyclerView ----------
class SimpleVH(v: View) : RecyclerView.ViewHolder(v)

class SimpleAdapter<T>(
    private val items: MutableList<T>,
    private val layoutRes: Int,
    private val binder: (View, T, Int) -> Unit
) : RecyclerView.Adapter<SimpleVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleVH =
        SimpleVH(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))

    override fun onBindViewHolder(holder: SimpleVH, position: Int) {
        binder(holder.itemView, items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<T>) {
        val copy = newItems.toList()
        items.clear()
        items.addAll(copy)
        notifyDataSetChanged()
    }
}
