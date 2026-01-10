package com.example.loginform

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userName = findViewById<EditText>(R.id.etName)
        val userPass = findViewById<EditText>(R.id.etPassword)

        val loginbtn = findViewById<Button>(R.id.loginButton)
        val registerbtn = findViewById<Button>(R.id.registerBtn)

        loginbtn.setOnClickListener {
            val uname = "tarun@gmail.com"
            val pass = "tarun123"

            Log.d(uname,"Username: ")
            Log.d(pass,"Password: ")

            if (userName.text.toString().equals(uname) && userPass.text.toString().equals(pass))
            {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            else
            {
                Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_LONG).show()
            }
        }

        registerbtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}