package com.mercacerca.app

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Formulario para crear o editar un producto (CRUD del vendedor y del administrador). */
class ProductFormActivity : AppCompatActivity() {

    private lateinit var img: ImageView

    // Abre la cámara del dispositivo y devuelve una vista previa de la foto.
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
        if (bmp != null) {
            ImageViewCompat.setImageTintList(img, null)
            img.setPadding(0, 0, 0, 0)
            img.setImageBitmap(bmp)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_form)

        val editing = intent.hasExtra("name")
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setTitle(if (editing) R.string.editar_producto else R.string.nuevo_producto)
        toolbar.setNavigationOnClickListener { finish() }

        img = findViewById(R.id.imgProduct)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val tilName = findViewById<TextInputLayout>(R.id.tilName)
        val etPrice = findViewById<TextInputEditText>(R.id.etPrice)
        val tilPrice = findViewById<TextInputLayout>(R.id.tilPrice)
        val etStock = findViewById<TextInputEditText>(R.id.etStock)
        val ddCategory = findViewById<AutoCompleteTextView>(R.id.ddCategory)

        val categories = listOf(
            getString(R.string.cat_cafe), getString(R.string.cat_lacteos), getString(R.string.cat_frutas),
            getString(R.string.cat_dulces), getString(R.string.cat_despensa)
        )
        ddCategory.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, categories))
        ddCategory.setText(intent.getStringExtra("category") ?: categories[0], false)

        if (editing) {
            etName.setText(intent.getStringExtra("name"))
            etPrice.setText(intent.getIntExtra("price", 0).toString())
            etStock.setText(intent.getIntExtra("stock", 0).toString())
        }

        findViewById<View>(R.id.btnPhoto).setOnClickListener {
            try {
                takePhoto.launch(null)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.biometria_no, Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<View>(R.id.btnSave).setOnClickListener {
            var valid = true
            if (etName.text.isNullOrBlank()) {
                tilName.error = getString(R.string.error_requerido); valid = false
            } else tilName.error = null
            if (etPrice.text.isNullOrBlank()) {
                tilPrice.error = getString(R.string.error_requerido); valid = false
            } else tilPrice.error = null

            if (valid) {
                Toast.makeText(this, R.string.guardado_ok, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
