package com.cambe.socket_sdk

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.cambe.socket_sdk.PATH_EVENT.EMIT_ROOM_LIVECHAT
import com.cambe.socket_sdk.PATH_EVENT.INCOMING_CHATLIST
import com.cambe.socket_sdk.PATH_EVENT.INCOMING_CONVERSATION
import com.cambe.socket_sdk.PATH_EVENT.JOIN_CHAT_LIST
import com.cambe.socket_sdk.PATH_EVENT.JOIN_CONVERSATION
import com.cambe.socket_sdk.PATH_EVENT.JOIN_ROOM_LIVECHAT
import com.cambe.socket_sdk.PATH_EVENT.LEAVE_CHATLIST
import com.cambe.socket_sdk.PATH_EVENT.LEAVE_CONVERSATION
import com.cambe.socket_sdk.PATH_EVENT.LEAVE_ROOM_LIVECHAT
import com.cambe.socket_sdk.PATH_EVENT.MEDIA_ANSWER
import com.cambe.socket_sdk.PATH_EVENT.MEDIA_OFFER
import com.cambe.socket_sdk.PATH_EVENT.REMOTE_PEER_ICE_CANDIDATE
import com.cambe.socket_sdk.PATH_EVENT.SEND_MESSAGE
import com.cambe.socket_sdk.PATH_EVENT.TYPING
import com.cambe.socket_sdk.PATH_EVENT.UPDATE_STATUS_MESSAGES
import com.cambe.socket_sdk.PATH_EVENT.UPDATE_USER_LIST
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit

object SocketHandler {

    private lateinit var mSocket: Socket
    private var socketPresenter: SocketPresenter? = null

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

            setupEventListeners()

        } catch (e: URISyntaxException) {
            Log.e("SocketHandler", "URISyntaxException: ${e.message}", e)
            throw RuntimeException("URISyntaxException: ${e.message}", e)
        } catch (e: Exception) {
            Log.e("SocketHandler", "Exception: ${e.message}", e)
            throw RuntimeException("Exception: ${e.message}", e)
        }
    }

    private fun setupEventListeners() {
        mSocket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketHandler", "Connected to the server")
            socketPresenter?.onConnect()
        }
        mSocket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketHandler", "Disconnected from the server")
            socketPresenter?.onDisconnect()
        }
        mSocket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("SocketHandler", "Connection Error: ${it[0]}")
            socketPresenter?.onConnectError(it[0] as Throwable)
        }

        mSocket.on(JOIN_CHAT_LIST) { args ->
            val userId = args[0] as String
            Log.d("SocketHandler", "Joined chat list: $userId")
            socketPresenter?.onJoinChatList(userId)
        }

        mSocket.on(INCOMING_CHATLIST) { args ->
            val message = args[0] as JSONObject
            Log.d("SocketHandler", "Incoming chat list: $message")
            socketPresenter?.onIncomingChatList(message)
        }

        mSocket.on(UPDATE_STATUS_MESSAGES) { args ->
            val payload = args[0] as JSONObject
            Log.d("SocketHandler", "Update status messages: $payload")
            socketPresenter?.onUpdateStatusMessages(payload)
        }

        mSocket.on(JOIN_CONVERSATION) { args ->
            val conversationId = args[0] as String
            Log.d("SocketHandler", "Joined conversation: $conversationId")
            socketPresenter?.onJoinConversation(conversationId)
        }

        mSocket.on(INCOMING_CONVERSATION) { args ->
            val message = args[0] as JSONObject
            Log.d("SocketHandler", "Incoming conversation: $message")
            socketPresenter?.onIncomingConversation(message)
        }

        mSocket.on(TYPING) { args ->
            val payload = args[0] as JSONObject
            Log.d("SocketHandler", "Typing: $payload")
            socketPresenter?.onTyping(payload)
        }

        mSocket.on(JOIN_ROOM_LIVECHAT) { args ->
            val roomId = args[0] as String
            Log.d("SocketHandler", "Joined room live chat: $roomId")
            socketPresenter?.onJoinRoomLiveChat(roomId)
        }

        mSocket.on(EMIT_ROOM_LIVECHAT) { args ->
            val payload = args[0] as JSONObject
            Log.d("SocketHandler", "Emit room live chat: $payload")
            socketPresenter?.onEmitRoomLiveChat(payload)
        }

        mSocket.on(LEAVE_ROOM_LIVECHAT) { args ->
            val roomId = args[0] as String
            Log.d("SocketHandler", "Leave room live chat: $roomId")
            socketPresenter?.onLeaveRoomLiveChat(roomId)
        }

        mSocket.on(UPDATE_USER_LIST) { args ->
            val userIds = args[0] as JSONObject
            Log.d("SocketHandler", "Update user list: $userIds")
            socketPresenter?.onUpdateUserList(userIds)
        }

        mSocket.on(SEND_MESSAGE) { args ->
            val payload = args[0] as JSONObject
            Log.d("SocketHandler", "Send message: $payload")
            socketPresenter?.onSendMessage(payload)
        }

        mSocket.on(LEAVE_CHATLIST) { args ->
            val userId = args[0] as String
            Log.d("SocketHandler", "Leave chat list: $userId")
            socketPresenter?.onLeaveChatList(userId)
        }

        mSocket.on(LEAVE_CONVERSATION) { args ->
            val conversationId = args[0] as String
            Log.d("SocketHandler", "Leave conversation: $conversationId")
            socketPresenter?.onLeaveConversation(conversationId)
        }

        mSocket.on(MEDIA_OFFER) { args ->
            val data = args[0] as JSONObject
            Log.d("SocketHandler", "Media offer: $data")
            socketPresenter?.onMediaOffer(data)
        }

        mSocket.on(MEDIA_ANSWER) { args ->
            val data = args[0] as JSONObject
            Log.d("SocketHandler", "Media answer: $data")
            socketPresenter?.onMediaAnswer(data)
        }

        mSocket.on(REMOTE_PEER_ICE_CANDIDATE) { args ->
            val data = args[0] as JSONObject
            Log.d("SocketHandler", "Remote peer ICE candidate: $data")
            socketPresenter?.onRemotePeerIceCandidate(data)
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
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

    // Setters for SocketPresenter
    fun setSocketPresenter(presenter: SocketPresenter) {
        this.socketPresenter = presenter
    }
}
