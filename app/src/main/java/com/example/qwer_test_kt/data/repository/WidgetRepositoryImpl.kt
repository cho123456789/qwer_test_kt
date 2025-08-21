package com.example.qwer_test_kt.data.repository

import android.content.Context
import com.example.qwer_test_kt.domin.model.WidgetData
import com.example.qwer_test_kt.domin.repository.WidgetRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// 데이터를 가져오는 로직 구현
class WidgetRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WidgetRepository {

    override suspend fun getWidgetData(): WidgetData {
        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", null)
        val widgetType = sharedPrefs.getString("widgetType", null)

        return WidgetData(
            wallpaperUrl = wallpaperUrl,
            widgetType = widgetType,
        )
    }
}