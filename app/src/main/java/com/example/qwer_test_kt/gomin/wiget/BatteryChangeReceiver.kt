package com.example.qwer_test_kt.gomin.wiget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class BatteryChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        MainScope().launch {
            try {
                // 인텐트에서 배터리 상태를 가져옵니다.
                val status: Int
                val level: Int
                val scale: Int

                // 어떤 인텐트를 받았는지 확인하여 그에 맞는 로직을 실행합니다.
                when (intent.action) {
                    Intent.ACTION_POWER_DISCONNECTED,
                    Intent.ACTION_POWER_CONNECTED -> {
                        // 전원 연결/분리 이벤트의 경우, BatteryManager를 통해 최신 상태를 직접 가져옵니다.
                        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                        status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
                        level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        scale = 100 // BatteryManager.BATTERY_PROPERTY_CAPACITY는 이미 퍼센트 값입니다.
                    }
                    else -> {
                        // 일반적인 배터리 변경 이벤트의 경우 인텐트에서 값을 가져옵니다.
                        status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    }
                }

                val batteryPct = if (scale > 0) level.toFloat() / scale.toFloat() * 100 else level.toFloat()

                Log.d("BatteryChangeReceiver", "onReceive: level=$level, status=$status")

                // 배터리 정보 SharedPreferences에 저장
                val sharedPref = context.getSharedPreferences("battery_widget_pref", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("battery_level", batteryPct.roundToInt())
                    putInt("battery_status", status)
                    apply()
                }

                // 위젯 업데이트
                BatteryGlanceWidgetProvider().updateAll(context)
                Log.d("BatteryChangeReceiver", "GlanceAppWidget update finished.")

            } finally {
                pendingResult.finish()
            }
        }
    }
}