package com.mercacerca.app

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

/** Rol Administrador: navegación con Navigation Drawer. */
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.abrir_menu, R.string.cerrar_menu)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val nav = findViewById<NavigationView>(R.id.navView)
        nav.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> showFragment(DashboardFragment(), R.string.dashboard)
                R.id.nav_users -> showFragment(CrudListFragment.newInstance(CrudListFragment.USERS), R.string.usuarios)
                R.id.nav_products -> showFragment(CrudListFragment.newInstance(CrudListFragment.PRODUCTS_ADMIN), R.string.productos)
                R.id.nav_sales -> showFragment(SalesReportFragment(), R.string.reporte_ventas)
                R.id.nav_logout -> logout()
            }
            drawer.closeDrawers()
            true
        }

        onBackPressedDispatcher.addCallback(this) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }

        if (savedInstanceState == null) {
            nav.setCheckedItem(R.id.nav_dashboard)
            showFragment(DashboardFragment(), R.string.dashboard)
        }
    }
}
