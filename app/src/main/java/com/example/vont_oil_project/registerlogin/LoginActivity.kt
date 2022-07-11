package com.example.vont_oil_project.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.vont_oil_project.R
import com.example.vont_oil_project.databinding.ActivityLoginBinding
import com.example.vont_oil_project.navdrawer.DashboardActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButtonLogin.setOnClickListener {
            performLogin()

        }

        binding.backToRegisterTextview.setOnClickListener{
            finish()
        }


    }

    private fun performLogin() {
        val email = binding.emailEditTextLogin.text.toString()
        val password = binding.passwordEditTextLogin.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}