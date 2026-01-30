package com.example.loginform

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val frag_btn = findViewById<Button>(R.id.frag_trigger_id)
        val recycle_btn = findViewById<Button>(R.id.btn_recycler_id)
        val notif_btn = findViewById<Button>(R.id.notif_btn_id)

        frag_btn.setOnClickListener {
            startActivity(Intent(this, FragmentActivity::class.java))
        }

        recycle_btn.setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }

        notif_btn.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }


    }
}