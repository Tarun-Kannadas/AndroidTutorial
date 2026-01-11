package com.example.loginform.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DialogModifier(context: Context): AlertDialog.Builder(context)
{
    enum class ResponseType{YES,NO,CANCEL}

    lateinit var onResponse : (response: ResponseType) -> Unit

    fun showDialog(title: String, message:String, listener:(responseType: ResponseType)->Unit)
    {
        val builder = AlertDialog.Builder(context)

        builder.apply{
            setTitle(title)
            setMessage(message)
            setIcon(android.R.drawable.ic_dialog_alert)
            onResponse = listener
        }

        builder.setPositiveButton("Yes"){
            dialog, id -> listener(ResponseType.YES)
        }

        builder.setNegativeButton("Abort"){
            _,_ -> listener(ResponseType.NO)
        }

        builder.setNeutralButton("Cancel")
        {
            _,_ -> listener(ResponseType.CANCEL)
        }

        val alertDialor = builder.create()
        alertDialor.setCancelable(true)
        alertDialor.show()
    }

}