package com.mercacerca.app

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Pasarela de pagos (solo interfaz: la integración con la API de pagos va en la siguiente etapa). */
class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.pasarela_pago)
        toolbar.setNavigationOnClickListener { finish() }

        findViewById<TextView>(R.id.tvTotal).text = intent.getIntExtra("total", 0).cop()

        val layoutCard = findViewById<View>(R.id.layoutCard)
        findViewById<RadioGroup>(R.id.rgMethod).setOnCheckedChangeListener { _, checkedId ->
            layoutCard.visibility = if (checkedId == R.id.rbCard) View.VISIBLE else View.GONE
        }

        val tilCard = findViewById<TextInputLayout>(R.id.tilCardNumber)
        val etCard = findViewById<TextInputEditText>(R.id.etCardNumber)
        val tilAddress = findViewById<TextInputLayout>(R.id.tilAddress)
        val etAddress = findViewById<TextInputEditText>(R.id.etAddress)
        val rgMethod = findViewById<RadioGroup>(R.id.rgMethod)

        findViewById<View>(R.id.btnPay).setOnClickListener {
            var valid = true
            if (etAddress.text.isNullOrBlank()) {
                tilAddress.error = getString(R.string.error_requerido); valid = false
            } else tilAddress.error = null

            if (rgMethod.checkedRadioButtonId == R.id.rbCard && (etCard.text?.length ?: 0) < 13) {
                tilCard.error = getString(R.string.error_requerido); valid = false
            } else tilCard.error = null

            if (valid) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.pago_exitoso)
                    .setMessage(R.string.pago_exitoso_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.aceptar) { _, _ ->
                        DemoData.cart.clear()
                        finish()
                    }
                    .show()
            }
        }
    }
}
