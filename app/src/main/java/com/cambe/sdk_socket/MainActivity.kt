package com.cambe.sdk_socket

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.cambe.socket_sdk.Service.SocketService
import com.cambe.socket_sdk.SocketHandler

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, SocketService::class.java)
        startForegroundService(intent)

        SocketHandler.setSocket( "URL","PATH_URL")
        SocketHandler.establishConnection()
    }
}