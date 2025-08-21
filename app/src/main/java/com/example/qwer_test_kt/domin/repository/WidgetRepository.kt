package com.example.qwer_test_kt.domin.repository

import android.graphics.Bitmap
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.WidgetData

// 데이터 계층 통신 하기위한 인터페이스 정의
interface WidgetRepository {
    suspend fun getWidgetData() : WidgetData
}