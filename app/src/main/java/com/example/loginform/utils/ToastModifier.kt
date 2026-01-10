package com.example.loginform.utils

import android.app.Activity
import android.os.Message
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.loginform.R

fun Toast.showCustomToast(message: String, activity: Activity)
{
    val toastLayout = activity.layoutInflater.inflate(
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.id_custom_layout)
    )

    val textToast = toastLayout.findViewById<TextView>(R.id.tv_custom_id)

    textToast.text = message

    val icon = toastLayout.findViewById<ImageView>(R.id.iv_custom_id)
    icon.setImageResource(R.drawable.notification)

    this.apply{
        setGravity(Gravity.BOTTOM, 0, 100)
        duration = Toast.LENGTH_LONG
        view = toastLayout
        show()
    }
}