package com.example.aplicativomessenger

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        buttonLogin.setOnClickListener {
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            Log.d("Login", "Tentativa de login com email: $email")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        Log.d("Main", "Usuario não cadstrado:}")
                    }
                    .addOnFailureListener {
                        Log.d("Main", "Não foi possível fazer o login: ${it.message}")
                    }
        }
        volta_para_o_cadastro.setOnClickListener {
            finish()
        }
    }
}