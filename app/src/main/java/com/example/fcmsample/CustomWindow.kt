package com.example.fcmsample

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.widget.Button


class CustomWindow(private val context: Context) {
    private var view: View
    private lateinit var params: WindowManager.LayoutParams
    private var windowManager: WindowManager
    private var layoutInflater: LayoutInflater

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT,
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.window_popup, null)
        view.findViewById<Button>(R.id.closeBtn).setOnClickListener { close() }
        params.gravity = Gravity.CENTER
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun open() {
        try {
            if (view.windowToken == null) {
                if (view.parent == null) {
                    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    val isScreenOn =
                        if (Build.VERSION.SDK_INT >= 20) pm.isInteractive else pm.isScreenOn
                    val flags = (PowerManager.FULL_WAKE_LOCK
                            or PowerManager.ACQUIRE_CAUSES_WAKEUP
                            or PowerManager.ON_AFTER_RELEASE)
                    if (!isScreenOn) {
                        pm.newWakeLock(flags, "FCMSample:full_lock").acquire(20000)
                        pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"FCMSample:full_cpu_lock").acquire(20000)
                    }
                    windowManager.addView(view, params)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    private fun close() {
        try {

            val intent = Intent(context, ForegroundService::class.java)
            intent.action = ForegroundService.ACTION_STOP_FOREGROUND_SERVICE
            context.startService(intent)
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(view)
            view.invalidate()
            (view.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }
}