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
import com.example.loginform.utils.DialogModifier
import com.example.loginform.utils.showCustomToast

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn = findViewById<Button>(R.id.btn_custom_id)
        
        val txtMessageId = findViewById<EditText>(R.id.et_custom_toast)

        val txtMsg = txtMessageId.text

        val btnDialog = findViewById<Button>(R.id.btn_custom_dialog)

        btnDialog.setOnClickListener {
            DialogModifier(this).showDialog(
                title = "Welcome to Dialog Box",
                message = "Happy New Year 2026")
            {
                val result = it.toString()

                if(result == "YES")
                {
                    Toast.makeText(this,"Dialog Accepted",Toast.LENGTH_SHORT).show()
                }
                else if(result == "NO")
                {
                    Toast.makeText(this,"Dialog Rejected",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else
                {
                    Toast.makeText(this,"Dialog Cancelled",Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn.setOnClickListener {
            Toast(this).showCustomToast(
                txtMsg.toString(),
                this
            )
        }
    }
}