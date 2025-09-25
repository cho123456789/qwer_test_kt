package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.qwer_test_kt.MainActivity
import com.example.qwer_test_kt.R

class GoBatteryMonitorService : Service() {
    private lateinit var batteryReceiver: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, GoBatteryWidgetProvider::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

                    // 위젯 프로바이더의 onUpdate 메소드를 직접 호출
                    GoBatteryWidgetProvider().onUpdate(context, appWidgetManager, appWidgetIds)
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)

        startForeground(1, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val channelId = "battery_monitor_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Battery Monitor Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return Notification.Builder(this, channelId)
            .setContentTitle("배터리 상태 모니터링 중")
            .setContentText("배터리 상태를 실시간 감지하고 있습니다.")
            .setSmallIcon(R.drawable.ma)
            .setContentIntent(pendingIntent)
            .build()
    }
}