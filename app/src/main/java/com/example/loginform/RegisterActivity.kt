package com.example.loginform

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginform.database.LoginDatabase
import com.example.loginform.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_login = findViewById<Button>(R.id.log_btn)
        val btn_reg = findViewById<Button>(R.id.reg_btn)
        val user_name = findViewById<EditText>(R.id.reg_username)
        val email = findViewById<EditText>(R.id.reg_email)
        val phn_num = findViewById<EditText>(R.id.reg_number)
        val pass = findViewById<EditText>(R.id.reg_pass)

        val db = LoginDatabase.getDatabase(this)
        val userDao = db.UserDao()

        btn_reg.setOnClickListener {

            val username = user_name.text.toString()
            val password = pass.text.toString()
            val u_email = email.text.toString()
            val u_num = phn_num.text.toString()

            if(username.isNotEmpty() && u_email.isNotEmpty() && u_num.isNotEmpty() && password.isNotEmpty())
            {
                val user = User(
                    id = 0,
                    username,
                    u_email,
                    u_num,
                    password
                )

                CoroutineScope(Dispatchers.IO).launch {
                    userDao.registerUser(user)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity,"Registration Successfull!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        btn_login.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}