package com.cambe.socket_sdk.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.cambe.socket_sdk.R
import com.cambe.socket_sdk.SocketHandler
import com.cambe.socket_sdk.SocketPresenter
import org.json.JSONObject

class SocketService : Service(),SocketPresenter {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Inisialisasi SocketHandler
        SocketHandler.setSocketPresenter(this)
        SocketHandler.establishConnection()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        return START_STICKY
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "SocketServiceChannel",
                "Socket Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, "SocketServiceChannel")
            .setContentTitle("Socket Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.ic_notifications) // Ganti dengan ikon yang sesuai
            .build()

        startForeground(1, notification)
    }

    private fun showNotification(title: String, content: String) {
        val notification: Notification = NotificationCompat.Builder(this, "SocketServiceChannel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notifications) // Ganti dengan ikon yang sesuai
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(2, notification) // Unique ID for this notification
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }


}
