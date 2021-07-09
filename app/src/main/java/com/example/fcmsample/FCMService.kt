package com.example.fcmsample

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendBroadcast(Intent("com.example.fcmsample.NOTIFY").apply {
            `package` = "com.example.fcmsample"
            putExtra("message", message.data["message"])
        })
        Log.d("FCMService", "onNewToken: Token: ${message.data["message"]}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMService", "onNewToken: Token: $token")
    }
}