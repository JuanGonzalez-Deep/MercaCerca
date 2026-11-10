package com.mercacerca.app

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RecoverPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        findViewById<android.view.View>(R.id.btnBack).setOnClickListener { finish() }

        val til = findViewById<TextInputLayout>(R.id.tilEmail)
        val et = findViewById<TextInputEditText>(R.id.etEmail)
        findViewById<android.view.View>(R.id.btnSend).setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(et.text.toString().trim()).matches()) {
                til.error = null
                Toast.makeText(this, R.string.enlace_enviado, Toast.LENGTH_LONG).show()
                finish()
            } else {
                til.error = getString(R.string.error_email)
            }
        }
    }
}
