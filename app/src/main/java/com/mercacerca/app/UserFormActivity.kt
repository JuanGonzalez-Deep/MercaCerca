package com.mercacerca.app

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Formulario para crear o editar un usuario (CRUD del administrador). */
class UserFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_form)

        val editing = intent.hasExtra("email")
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setTitle(if (editing) R.string.editar_usuario else R.string.nuevo_usuario)
        toolbar.setNavigationOnClickListener { finish() }

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val tilName = findViewById<TextInputLayout>(R.id.tilName)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val ddRole = findViewById<AutoCompleteTextView>(R.id.ddRole)

        val roles = listOf(
            getString(R.string.rol_comprador),
            getString(R.string.rol_vendedor),
            getString(R.string.rol_admin)
        )
        ddRole.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, roles))
        ddRole.setText(intent.getStringExtra("role") ?: roles[0], false)

        if (editing) {
            etName.setText(intent.getStringExtra("name"))
            etEmail.setText(intent.getStringExtra("email"))
            findViewById<View>(R.id.tilPass).visibility = View.GONE
        }

        findViewById<View>(R.id.btnSave).setOnClickListener {
            var valid = true
            if (etName.text.isNullOrBlank()) {
                tilName.error = getString(R.string.error_requerido); valid = false
            } else tilName.error = null
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches()) {
                tilEmail.error = getString(R.string.error_email); valid = false
            } else tilEmail.error = null

            if (valid) {
                Toast.makeText(this, R.string.guardado_ok, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        findViewById<View>(R.id.btnCancel).setOnClickListener { finish() }
    }
}
