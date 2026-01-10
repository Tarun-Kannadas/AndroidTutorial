package com.example.loginform

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginform.utils.DialogModifier
import com.example.loginform.utils.showCustomToast
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManagerCompat
    var notificationChannelId: String = "DOWNLOAD_CHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createNotificationChannel()

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
            Toast(applicationContext).showCustomToast(
                txtMsg.toString(),
                this
            )
        }

        var btnNotification = findViewById<Button>(R.id.notify_status)

        notificationManager = NotificationManagerCompat.from(this)

        btnNotification.setOnClickListener {
            val homeIntent = Intent(this, HomeActivity::class.java)

            var pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,0,homeIntent, PendingIntent.FLAG_IMMUTABLE
            )

            val PROGRESS_MAX = 100

            var notification = NotificationCompat.Builder(this,notificationChannelId).apply{
                setSmallIcon(android.R.drawable.stat_sys_download)
                setContentTitle("Download Notification")
                setContentText("Downloading...")
                setPriority(NotificationCompat.PRIORITY_LOW)
                setOngoing(true)
                setOnlyAlertOnce(true)
                setProgress(PROGRESS_MAX, 0,true)
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
            else
            {
                lifecycleScope.launch {
                    SystemClock.sleep(2000)

                    var progress = 0

                    withContext(Dispatchers.IO){
                        while(progress < PROGRESS_MAX)
                        {
                            SystemClock.sleep(1000)
                            progress += 20
                            notification.setContentTitle("Progress:$progress%").setProgress(PROGRESS_MAX, progress,false)
                            notificationManager.notify(1, notification.build())
                        }
                    }

                    notification.setContentTitle("Download Status!").apply{
                        setProgress(PROGRESS_MAX,100, false)
                        setOngoing(false)
                        setContentText("Download Successful")
                        setSmallIcon(android.R.drawable.stat_sys_download_done)
                    }

                    notificationManager.notify(1,notification.build())
                }


                Thread({
//                    SystemClock.sleep(2000)
//
//                    var progress = 0
//
//                    while(progress < PROGRESS_MAX)
//                    {
//                        SystemClock.sleep(1000)
//                        progress += 20
//                        notification.setContentTitle("Progress:$progress%").setProgress(PROGRESS_MAX, progress,false)
//                        notificationManager.notify(1, notification.build())
//                    }
//                    notification.setContentTitle("Download Status!").apply{
//                        setProgress(PROGRESS_MAX,100, false)
//                        setOngoing(false)
//                        setContentText("Download Successful")
//                        setSmallIcon(android.R.drawable.stat_sys_download_done)
//                    }
//
//                    notificationManager.notify(1,notification.build())
                }).start()
            }

        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notificationChannelId,
                "Downloads", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}