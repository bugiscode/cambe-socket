package com.cambe.socket_sdk

import org.json.JSONObject

interface SocketPresenter {
    // Connection events
    fun onConnect(){}
    fun onDisconnect(){}
    fun onConnectError(error: Throwable){}

    // Chat events
    fun onJoinChatList(userId: String){}
    fun onIncomingChatList(message: JSONObject){}
    fun onUpdateStatusMessages(payload: JSONObject){}

    // Conversation events
    fun onJoinConversation(conversationId: String){}
    fun onIncomingConversation(message: JSONObject){}
    fun onTyping(payload: JSONObject){}

    // Room live chat events
    fun onJoinRoomLiveChat(roomId: String){}
    fun onEmitRoomLiveChat(payload: JSONObject){}
    fun onLeaveRoomLiveChat(roomId: String){}

    // User list events
    fun onUpdateUserList(userIds: JSONObject){}

    // Message events
    fun onSendMessage(payload: JSONObject){}

    // Leave events
    fun onLeaveChatList(userId: String){}
    fun onLeaveConversation(conversationId: String){}

    // Media events
    fun onMediaOffer(data: JSONObject){}
    fun onMediaAnswer(data: JSONObject){}
    fun onRemotePeerIceCandidate(data: JSONObject){}
}