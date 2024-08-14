package com.cambe.socket_sdk.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.cambe.socket_sdk.SocketHandler

class SocketService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Inisialisasi SocketHandler dan mulai koneksi
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
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer) // Ganti dengan icon yang sesuai
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}