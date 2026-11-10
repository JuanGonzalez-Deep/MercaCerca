package com.mercacerca.app

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var chipGroupRole: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        chipGroupRole = findViewById(R.id.chipGroupRole)

        findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()
            var valid = true

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = getString(R.string.error_email); valid = false
            } else tilEmail.error = null

            if (pass.length < 6) {
                tilPassword.error = getString(R.string.error_pass); valid = false
            } else tilPassword.error = null

            if (valid) openRoleHome()
        }

        findViewById<MaterialButton>(R.id.btnBiometric).setOnClickListener { authenticateWithBiometrics() }

        findViewById<android.view.View>(R.id.tvForgot).setOnClickListener {
            startActivity(Intent(this, RecoverPasswordActivity::class.java))
        }
    }

    /** Según el rol elegido se abre la pantalla principal correspondiente. */
    private fun openRoleHome() {
        val target = when (chipGroupRole.checkedChipId) {
            R.id.chipAdmin -> AdminActivity::class.java
            R.id.chipSeller -> SellerActivity::class.java
            else -> BuyerActivity::class.java
        }
        startActivity(Intent(this, target))
        finish()
    }

    private fun authenticateWithBiometrics() {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        if (BiometricManager.from(this).canAuthenticate(authenticators) != BiometricManager.BIOMETRIC_SUCCESS) {
            Toast.makeText(this, R.string.biometria_no, Toast.LENGTH_LONG).show()
            return
        }

        val prompt = BiometricPrompt(
            this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    openRoleHome()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    Toast.makeText(this@LoginActivity, R.string.biometria_error, Toast.LENGTH_SHORT).show()
                }
            })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometria_titulo))
            .setSubtitle(getString(R.string.biometria_sub))
            .setAllowedAuthenticators(authenticators)
            .build()
        prompt.authenticate(info)
    }
}
