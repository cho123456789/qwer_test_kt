package com.example.qwer_test_kt.gomin.wiget

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import coil.imageLoader
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
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

        val action = intent.action
        // WidgetPreferencesManager 사용
        val widgetPrefs = WidgetPreferencesManager.getInstance(context)
        val wallpaperUrl = widgetPrefs.getWallpaperUrl() ?: ""
        val widgetType = widgetPrefs.getWidgetType()

        if ((action == "com.example.qwer_test_kt.UPDATE_IMAGE" || action == Intent.ACTION_BOOT_COMPLETED)
            && wallpaperUrl.isNotEmpty()
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, GoWatchWidgetReceiver::class.java)
            )
            onUpdate(context, appWidgetManager, appWidgetIds)
        } else if (action == ACTION_UPDATE_TIME) {
            CoroutineScope(Dispatchers.IO).launch {
                val glanceIds =
                    GlanceAppWidgetManager(context).getGlanceIds(GoWatchWidgetProvider::class.java)
                glanceIds.forEach { glanceId ->
                    GoWatchWidgetProvider().update(context, glanceId)
                }
            }
            scheduleNextUpdate(context)
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

        val widgetPrefs = WidgetPreferencesManager.getInstance(context)
        val scope = CoroutineScope(Dispatchers.IO)

        // 각 위젯 ID에 대해 개별적으로 처리
        appWidgetIds.forEach { appWidgetId ->
            scope.launch {
                try {
                    // 각 위젯 ID에 해당하는 배경화면 URL 가져오기
                    var wallpaperUrl = widgetPrefs.getWallpaperUrl(appWidgetId)

                    // 위젯 ID별 데이터가 없으면 임시 공유 데이터 사용 (새로 추가된 위젯)
                    if (wallpaperUrl.isNullOrEmpty()) {
                        wallpaperUrl = widgetPrefs.getWallpaperUrl()
                        val widgetType = widgetPrefs.getWidgetType()
                        val position = widgetPrefs.getWidgetPosition()
                        val textColor = widgetPrefs.getTextColor()

                        if (!wallpaperUrl.isNullOrEmpty()) {
                            // 임시 데이터를 이 위젯 ID로 저장 (모든 관련 데이터 포함)
                            widgetPrefs.saveWidgetData(
                                appWidgetId,
                                wallpaperUrl,
                                widgetType ?: "clock",
                                position
                            )
                            widgetPrefs.setTextColor(appWidgetId, textColor)
                        } else {
                            return@launch
                        }
                    }
                    // appWidgetId에 해당하는 glanceId 가져오기
                    val glanceId = GlanceAppWidgetManager(context)
                        .getGlanceIdBy(appWidgetId)

                    try {
                        // 이미지 파일 캐시 확인 - 이미 있으면 다운로드 스킵
                        val cachedFile =
                            File(context.cacheDir, "watch_wallpaper_${appWidgetId}.jpg")

                        // URL 변경 확인을 위한 해시 파일
                        val hashFile = File(context.cacheDir, "watch_wallpaper_${appWidgetId}.hash")
                        val currentHash = wallpaperUrl.hashCode().toString()
                        val cachedHash = if (hashFile.exists()) hashFile.readText() else ""

                        val imageFile = if (cachedFile.exists() && currentHash == cachedHash) {
                            cachedFile
                        } else {
                            if (cachedFile.exists()) {
                                cachedFile.delete()
                            }

                            val bitmap = withContext(Dispatchers.IO) {
                                downloadBitmap(context, wallpaperUrl)
                            } ?: throw IllegalStateException("비트맵 다운로드 실패")

                            val savedFile = withContext(Dispatchers.IO) {
                                saveBitmapToCacheFile(context, bitmap, appWidgetId)
                            }

                            // URL 해시 저장
                            hashFile.writeText(currentHash)
                            savedFile
                        }

                        updateAppWidgetState(
                            context = context,
                            definition = PreferencesGlanceStateDefinition,
                            glanceId = glanceId
                        ) { prefs ->
                            prefs.toMutablePreferences().apply {
                                this[ImageUrlKey] = imageFile.absolutePath
                            }
                        }
                        GoWatchWidgetProvider().update(context, glanceId)

                    } catch (e: Exception) {
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)

        val widgetPrefs = WidgetPreferencesManager.getInstance(context)
        appWidgetIds.forEach { appWidgetId ->
            widgetPrefs.clearWidgetData(appWidgetId)

            // 캐시된 이미지 파일도 삭제
            val cachedFile = File(context.cacheDir, "watch_wallpaper_${appWidgetId}.jpg")
            if (cachedFile.exists()) {
                cachedFile.delete()
            }

            // 해시 파일도 삭제
            val hashFile = File(context.cacheDir, "watch_wallpaper_${appWidgetId}.hash")
            if (hashFile.exists()) {
                hashFile.delete()
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
            .size(1080, 1920) // 위젯에 적합한 크기로 제한
            .build()
        val result = context.imageLoader.execute(request)
        return (result.drawable as? BitmapDrawable)?.bitmap
    }

    private fun saveBitmapToCacheFile(context: Context, bitmap: Bitmap, widgetId: Int): File {
        val cacheFile = File(context.cacheDir, "watch_wallpaper_${widgetId}.jpg")

        // 기존 파일이 있으면 삭제
        if (cacheFile.exists()) {
            cacheFile.delete()
        }

        // 위젯에 적합한 크기로 리사이징 (더 빠른 로딩)
        val maxWidth = 1080
        val maxHeight = 1920
        val scaledBitmap = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
            val scale = minOf(
                maxWidth.toFloat() / bitmap.width,
                maxHeight.toFloat() / bitmap.height
            )
            val newWidth = (bitmap.width * scale).toInt()
            val newHeight = (bitmap.height * scale).toInt()
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }

        val fos = FileOutputStream(cacheFile)
        // JPEG 사용으로 파일 크기 대폭 감소 (빠른 로딩)
        scaledBitmap.compress(CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()

        // 리사이징한 비트맵이 원본과 다르면 메모리 해제
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }

        return cacheFile
    }

    companion object {
        const val ACTION_UPDATE_TIME = "com.example.qwer_test_kt.UPDATE_TIME"
        const val ACTION_UPDATE_IMAGE = "com.example.qwer_test_kt.UPDATE_IMAGE"
    }
}
