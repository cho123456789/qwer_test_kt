package com.example.qwer_test_kt


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetProvider

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            GoWatchWidgetProvider().scheduleNextUpdate(context)
        }
    }
}