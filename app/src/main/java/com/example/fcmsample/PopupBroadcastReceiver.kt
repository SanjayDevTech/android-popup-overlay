package com.example.fcmsample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.startForegroundService

class PopupBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message") ?: "No message"
        Log.d("PopupBroadcastReceiver", "onReceive: message received: $message")
        if (context == null) {
            Log.d("PopupBroadcastReceiver", "onReceive: Context is null")
            return
        }
//        Intent(context, BlankActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            putExtra("message", message)
//            context.startActivity(this)
//        }
        startService(context, message)
    }

    private fun startService(context: Context, message: String) {
        val intent = Intent(context, ForegroundService::class.java).apply {
            putExtra("message", message)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } else {
                Log.d("BlankActivity", "startService: no overlay permission")
            }
        } else {
            context.startService(intent)
        }
    }
}