package com.example.qwer_test_kt.gomin.wiget

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryInfoManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _isCharging = MutableStateFlow(false)
    val isCharging = _isCharging.asStateFlow()

    private val batteryReceiver: BatteryReceiver by lazy { BatteryReceiver() }

    init {
        // 앱이 처음 시작될 때 리시버를 등록합니다.
        // 실제 앱에서는 onResume/onPause 또는 Service/Application 클래스에 연결하는 것이 더 안정적입니다.
        registerReceiver()
        updateInitialStatus()
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        try {
            context.registerReceiver(batteryReceiver, intentFilter)
            Log.d("BatteryInfoManager", "BroadcastReceiver registered.")
        } catch (e: Exception) {
            Log.e("BatteryInfoManager", "Error registering receiver", e)
        }
    }

    // 초기 상태를 한 번 업데이트합니다.
    private fun updateInitialStatus() {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
            context.registerReceiver(null, it)
        }
        batteryStatus?.let {
            val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            _isCharging.value = isCharging
        }
    }

    // 내부 클래스로 BroadcastReceiver를 구현합니다.
    private inner class BatteryReceiver : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL

            if (_isCharging.value != isCharging) {
                _isCharging.value = isCharging
                Log.d("BatteryInfoManager", "Charging status changed to: $isCharging")
            }
        }
    }

    // 이 메서드는 Hilt가 싱글톤을 파괴할 때 호출되지 않습니다.
    // 따라서 실제 앱에서는 앱의 라이프사이클에 맞춰 리시버를 등록/해제해야 합니다.
    fun unregisterReceiver() {
        try {
            context.unregisterReceiver(batteryReceiver)
            Log.d("BatteryInfoManager", "BroadcastReceiver unregistered.")
        } catch (e: IllegalArgumentException) {
            Log.e("BatteryInfoManager", "Receiver not registered or already unregistered", e)
        }
    }
}