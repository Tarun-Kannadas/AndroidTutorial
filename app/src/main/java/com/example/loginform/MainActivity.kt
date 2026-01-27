package com.example.loginform

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.loginform.database.LoginDatabase
import com.example.loginform.repository.UserRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()

        // UI References
        val userName = findViewById<EditText>(R.id.etName)
        val userPass = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        // Initialize Repository
        val db = LoginDatabase.getDatabase(this)
        val repository = UserRepository(db.UserDao())

        loginBtn.setOnClickListener {
            // Trim inputs to remove accidental leading/trailing spaces
            val uname = userName.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (uname.isNotEmpty() && pass.isNotEmpty()) {
                // Disable button to prevent multiple clicks during processing
                loginBtn.isEnabled = false

                lifecycleScope.launch {
                    try {
                        val user = repository.authUser(uname, pass)

                        if (user != null) {
                            Toast.makeText(this@MainActivity, "Welcome, ${user.username}!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@MainActivity, HomeActivity::class.java).apply {
                                putExtra("username", user.username)
                            }
                            startActivity(intent)
                            finish() // Kill MainActivity so user can't "Back" into the login screen
                        } else {
                            Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } finally {
                        // Re-enable button if login fails
                        loginBtn.isEnabled = true
                    }
                }
            } else {
                Toast.makeText(this, "Please enter both credentials", Toast.LENGTH_SHORT).show()
            }
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupWindowInsets() {
        // Ensure the layout respects system bars (status bar, navigation bar)
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}