package com.mercacerca.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

/** Rol Vendedor: navegación con Bottom Navigation. */
class SellerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))

        val bottom = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_seller_products ->
                    showFragment(CrudListFragment.newInstance(CrudListFragment.PRODUCTS_SELLER), R.string.mis_productos)
                R.id.nav_seller_orders ->
                    showFragment(CrudListFragment.newInstance(CrudListFragment.ORDERS), R.string.pedidos)
                else -> showFragment(ProfileFragment.newInstance("seller"), R.string.perfil)
            }
            true
        }
        if (savedInstanceState == null) bottom.selectedItemId = R.id.nav_seller_products
    }
}
