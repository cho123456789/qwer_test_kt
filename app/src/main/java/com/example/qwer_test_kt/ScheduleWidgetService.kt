package com.example.qwer_test_kt

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.RequiresApi
import java.time.LocalDate

class ScheduleWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ScheduleFactory(applicationContext)
    }
}

// ScheduleFactory.kt
class ScheduleFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var scheduleList: List<String> = listOf()

    override fun onCreate() {}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDataSetChanged() {
        val prefs = context.getSharedPreferences("qwer_schedule_prefs", Context.MODE_PRIVATE)
        val year = prefs.getInt("year", LocalDate.now().year)
        val month = prefs.getInt("month", LocalDate.now().monthValue)

        scheduleList = getScheduleFor(year, month)
    }

    override fun getCount() = scheduleList.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_schedule)
        rv.setTextViewText(R.id.text_schedule_item, scheduleList[position])
        return rv
    }

    override fun getLoadingView() = null
    override fun getViewTypeCount() = 1
    override fun getItemId(position: Int) = position.toLong()
    override fun hasStableIds() = true
    override fun onDestroy() {}

    private fun getScheduleFor(year: Int, month: Int): List<String> {
        val scheduleMap = mapOf(
            Pair(2025, 8) to listOf(
                "8월 5일 (화) - 펜타포트 록페스티벌",
                "8월 6일 (수) - 팬사인회 (서울)",
                "8월 7일 (목) - 팬사인회 (부산)",
                "8월 10일 (일) - 컴백 무대 (엠카)",
                "8월 15일 (금) - 음방 사전녹화"
            ),
            Pair(2025, 7) to listOf(
                "7월 22일 (월) - 컴백 예고 공개",
                "7월 25일 (목) - 티저 이미지 공개"
            )
        )
        return scheduleMap[Pair(year, month)] ?: listOf("일정 없음")
    }
}