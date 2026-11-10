package com.mercacerca.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

/** Rol Comprador: navegación con Bottom Navigation. */
class BuyerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))

        val bottom = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> showFragment(HomeFragment(), R.string.app_name)
                R.id.nav_catalog -> showFragment(CatalogFragment(), R.string.catalogo)
                R.id.nav_cart -> showFragment(CartFragment(), R.string.carrito)
                else -> showFragment(ProfileFragment.newInstance("buyer"), R.string.perfil)
            }
            true
        }
        if (savedInstanceState == null) bottom.selectedItemId = R.id.nav_home
    }
}
