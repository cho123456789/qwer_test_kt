package com.example.qwer_test_kt.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.qwer_test_kt.domin.model.BatteryInfo
import com.example.qwer_test_kt.domin.repository.BatteryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// 데이터를 가져오는 로직 구현
class BatteryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BatteryRepository {

    override suspend fun getBatteryInfo(): BatteryInfo {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }
        val chargingStatus = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

        return BatteryInfo(level.toFloat(), chargingStatus)
    }
}