package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

val ImageUrlKey = stringPreferencesKey("widget_image_url")

class GoWatchWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GoWatchWidgetProvider()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_UPDATE_TIME -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val glanceIds =
                        GlanceAppWidgetManager(context).getGlanceIds(GoWatchWidgetProvider::class.java)
                    glanceIds.forEach { glanceId ->
                        GoWatchWidgetProvider().update(context, glanceId)
                    }
                }
                scheduleNextUpdate(context)
            }
            // 이 ACTION은 앱 설정에서 이미지 변경 시 호출되도록 설계
            ACTION_UPDATE_IMAGE -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    android.content.ComponentName(context, GoWatchWidgetReceiver::class.java)
                )
                onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
}

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        // 위젯이 처음 추가될 때만 알람을 설정하도록 변경
        scheduleNextUpdate(context)

        val sharedPrefs = context.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", "") ?: ""

        if (wallpaperUrl.isEmpty()) {
            Log.e("GoWatchWidgetReceiver", "배경화면 URL이 비어있습니다. 업데이트를 건너뜁니다.")
            return
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val glanceIds = GlanceAppWidgetManager(context)
                .getGlanceIds(GoWatchWidgetProvider::class.java)

            glanceIds.forEach { glanceId ->
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        downloadBitmap(context, wallpaperUrl)
                    } ?: throw IllegalStateException("비트맵 다운로드 실패")

                    val file = withContext(Dispatchers.IO) {
                        saveBitmapToTempFile(context, bitmap)
                    }
                    updateAppWidgetState(
                        context = context,
                        definition = PreferencesGlanceStateDefinition,
                        glanceId = glanceId
                    ) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[ImageUrlKey] = file.absolutePath
                        }
                    }
                    GoWatchWidgetProvider().update(context, glanceId)

                } catch (e: Exception) {
                    Log.e("GoWatchWidgetReceiver", "위젯 배경화면 업데이트 실패", e)
                }
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNextUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, GoWatchWidgetReceiver::class.java).apply {
            action = ACTION_UPDATE_TIME
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 기존 알람을 취소하여 중복 방지
        alarmManager.cancel(pendingIntent)

        // Android 12 이상에서 정확한 알람 스케줄링 가능 여부 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("GoWatchWidgetReceiver", "정확한 알람을 설정할 수 없습니다. 부정확한 반복 알람을 사용합니다.")
                scheduleInexactUpdate(context, alarmManager)
                return
            }
        }

        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val triggerAtMillis = calendar.timeInMillis

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            Log.e("GoWatchWidgetReceiver", "정확한 알람 설정 시 보안 예외 발생", e)
            scheduleInexactUpdate(context, alarmManager)
        }
    }

    private fun scheduleInexactUpdate(context: Context, alarmManager: AlarmManager) {
        val intent = Intent(context, GoWatchWidgetReceiver::class.java).apply {
            action = ACTION_UPDATE_TIME
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
    }

    private suspend fun downloadBitmap(context: Context, url: String): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        val result = context.imageLoader.execute(request)
        return (result.drawable as? BitmapDrawable)?.bitmap
    }

    private fun saveBitmapToTempFile(context: Context, bitmap: Bitmap): File {
        val tempFile = File(context.cacheDir, "wallpaper_temp_${System.currentTimeMillis()}.png")
        val fos = FileOutputStream(tempFile)
        // PNG로 저장하여 품질 저하 방지 (100% 품질)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return tempFile
    }

    companion object {
        const val ACTION_UPDATE_TIME = "com.example.qwer_test_kt.UPDATE_TIME"
        const val ACTION_UPDATE_IMAGE = "com.example.qwer_test_kt.UPDATE_IMAGE"
    }
}
