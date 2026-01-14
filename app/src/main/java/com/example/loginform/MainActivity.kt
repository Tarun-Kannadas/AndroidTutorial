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
import com.example.loginform.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val db = AppDatabase.getDatabase(this)
        val userDao = db.UserDao()

        loginbtn.setOnClickListener {
            val uname = userName.text.toString()
            val pass = userPass.text.toString()

            if (uname.isNotEmpty() && pass.isNotEmpty())
            {
                CoroutineScope(Dispatchers.IO).launch {

                    val user = userDao.loginUser(uname, pass)

                    withContext(Dispatchers.Main)
                    {
                        if(user != null){
                            Toast.makeText(this@MainActivity,"Login Successful", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            intent.putExtra("username", user.username)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this@MainActivity,"Invalid Username or Password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Enter all fields!",Toast.LENGTH_LONG).show()
            }
        }

        registerbtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}