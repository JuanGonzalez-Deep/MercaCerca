package com.mercacerca.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val name = intent.getStringExtra("name") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val price = intent.getIntExtra("price", 0)
        val seller = intent.getStringExtra("seller") ?: ""

        findViewById<TextView>(R.id.tvName).text = name
        findViewById<TextView>(R.id.tvCategory).text = category
        findViewById<TextView>(R.id.tvPrice).text = price.cop()
        findViewById<TextView>(R.id.tvSeller).text = getString(R.string.vendido_por) + " " + seller

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<View>(R.id.btnAddCart).setOnClickListener {
            DemoData.addToCart(name, price)
            Toast.makeText(this, R.string.agregado_carrito, Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.btnBuy).setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java).putExtra("total", price + 5000))
        }
    }
}
