package com.cambe.socket_sdk

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit

object SocketHandler {

    private lateinit var mSocket: Socket

    @SuppressLint("StaticFieldLeak")
    @Synchronized
    fun setSocket(serverUrl: String, path: String = "/socket.io") {
        try {
            val okHttpClient = createOkHttpClient()
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient)
            IO.setDefaultOkHttpCallFactory(okHttpClient)
            val options = IO.Options().apply {
                transports = arrayOf("websocket")
                this.path = path
            }
            mSocket = IO.socket(serverUrl, options)

            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketHandler", "Connected to the server")
            }
            mSocket.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketHandler", "Disconnected from the server")
            }
            mSocket.on(Socket.EVENT_CONNECT_ERROR) {
                Log.e("SocketHandler", "Connection Error: ${it[0]}")
            }

            // Implementing server events
            mSocket.on("join-chatlist") { args ->
                val userId = args[0] as String
                Log.d("SocketHandler", "Joined chat list: $userId")
            }

            mSocket.on("incoming-chatlist") { args ->
                val message = args[0] as JSONObject
                Log.d("SocketHandler", "Incoming chat list: $message")
            }

            mSocket.on("update-status-messages") { args ->
                val payload = args[0] as JSONObject
                Log.d("SocketHandler", "Update status messages: $payload")
            }

            mSocket.on("join-conversation") { args ->
                val conversationId = args[0] as String
                Log.d("SocketHandler", "Joined conversation: $conversationId")
            }

            mSocket.on("join-room-livechat") { args ->
                val roomId = args[0] as String
                Log.d("SocketHandler", "Joined room live chat: $roomId")
            }

            mSocket.on("emit-room-livechat") { args ->
                val payload = args[0] as JSONObject
                Log.d("SocketHandler", "Emit room live chat: $payload")
            }

            mSocket.on("send-message") { args ->
                val payload = args[0] as JSONObject
                Log.d("SocketHandler", "Send message: $payload")
            }

            mSocket.on("incoming-conversation") { args ->
                val message = args[0] as JSONObject
                Log.d("SocketHandler", "Incoming conversation: $message")
            }

            mSocket.on("typing") { args ->
                val payload = args[0] as JSONObject
                Log.d("SocketHandler", "Typing: $payload")
            }

            mSocket.on("leave-chatlist") { args ->
                val userId = args[0] as String
                Log.d("SocketHandler", "Leave chat list: $userId")
            }

            mSocket.on("leave-conversation") { args ->
                val conversationId = args[0] as String
                Log.d("SocketHandler", "Leave conversation: $conversationId")
            }

            mSocket.on("leave-room-livechat") { args ->
                val roomId = args[0] as String
                Log.d("SocketHandler", "Leave room live chat: $roomId")
            }

            mSocket.on("update-user-list") { args ->
                val userIds = args[0] as JSONObject
                Log.d("SocketHandler", "Update user list: $userIds")
            }

            mSocket.on("mediaOffer") { args ->
                val data = args[0] as JSONObject
                Log.d("SocketHandler", "Media offer: $data")
            }

            mSocket.on("mediaAnswer") { args ->
                val data = args[0] as JSONObject
                Log.d("SocketHandler", "Media answer: $data")
            }

            mSocket.on("remotePeerIceCandidate") { args ->
                val data = args[0] as JSONObject
                Log.d("SocketHandler", "Remote peer ICE candidate: $data")
            }

        } catch (e: URISyntaxException) {
            Log.e("SocketIO", "URISyntaxException: ${e.message}", e)
            throw RuntimeException("URISyntaxException: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("SocketIO", "Exception: ${e.message}", e)
            throw RuntimeException("Exception: ${e.message}", e)
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
//            hostnameVerifier { _, _ -> true }
//            sslSocketFactory(
//                SSLCertificateConfigurator.getSSLConfiguration(context).socketFactory,
//                getTrustManager(context)
//            )
            readTimeout(1, TimeUnit.MINUTES)
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }.build()
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        if (!mSocket.connected()) {
            mSocket.connect()
        }
    }

    @Synchronized
    fun closeConnection() {
        if (mSocket.connected()) {
            mSocket.disconnect()
            mSocket.off()
        }
    }

    @Synchronized
    fun sendMessage(event: String, data: JSONObject) {
        mSocket.emit(event, data)
    }
}