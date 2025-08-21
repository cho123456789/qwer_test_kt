package com.example.qwer_test_kt.domin.repository

import android.graphics.Bitmap
import com.example.qwer_test_kt.domin.model.BatteryInfo
import com.example.qwer_test_kt.domin.model.Member

// 데이터 계층 통신 하기위한 인터페이스 정의
interface BatteryRepository {
    suspend fun getBatteryInfo() : BatteryInfo
}