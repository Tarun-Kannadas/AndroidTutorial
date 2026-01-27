package com.example.loginform

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loginform.data.User
import com.example.loginform.database.LoginDatabase
import com.example.loginform.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // UI References
        val btnLogin = findViewById<Button>(R.id.log_btn)
        val btnReg = findViewById<Button>(R.id.reg_btn)
        val userName = findViewById<EditText>(R.id.reg_username)
        val email = findViewById<EditText>(R.id.reg_email)
        val phnNum = findViewById<EditText>(R.id.reg_number)
        val pass = findViewById<EditText>(R.id.reg_pass)

        // Initialize Repository instead of DAO directly
        val db = LoginDatabase.getDatabase(this)
        val repository = UserRepository(db.UserDao())

        btnReg.setOnClickListener {
            val etUsername = userName.text.toString()
            val etPassword = pass.text.toString()
            val etEmail = email.text.toString()
            val etNum = phnNum.text.toString()

            if (etUsername.isNotBlank() && etEmail.isNotBlank() && etNum.isNotBlank() && etPassword.isNotBlank()) {
                val user = User(
                    id = 0, // Room generates this if autoGenerate = true in your Entity
                    etUsername,
                    etEmail,
                    etNum,
                    etPassword
                )

                // lifecycleScope handles the thread switching for you
                lifecycleScope.launch {
                    try {
                        repository.registerUser(user)

                        Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate back to login
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Close registration activity
                    } catch (e: Exception) {
                        Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            // finish() is often better here if MainActivity is already in the backstack
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}