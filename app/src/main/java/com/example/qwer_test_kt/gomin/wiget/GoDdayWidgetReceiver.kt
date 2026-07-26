package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import coil.imageLoader
import coil.request.ImageRequest
import com.example.qwer_test_kt.gomin.util.WidgetKeys
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class GoDdayWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = GoDdayWidgetProvider()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action

        val widgetPrefs = WidgetPreferencesManager.getInstance(context)
        val wallpaperUrl = widgetPrefs.getWallpaperUrl() ?: ""

        if ((action == ACTION_UPDATE_DDAY || action == Intent.ACTION_BOOT_COMPLETED)
            && wallpaperUrl.isNotEmpty()
        ) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val allWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, GoDdayWidgetReceiver::class.java)
            )
            val targetWidgetId = intent.getIntExtra(EXTRA_APP_WIDGET_ID, -1)
            val appWidgetIds = if (targetWidgetId >= 0) intArrayOf(targetWidgetId) else allWidgetIds
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

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
                        val ddayTitle = widgetPrefs.getDdayTitle()
                        val ddayDate = widgetPrefs.getDdayDate()

                        if (!wallpaperUrl.isNullOrEmpty()) {
                            // 임시 데이터를 이 위젯 ID로 저장 (모든 관련 데이터 포함)
                            widgetPrefs.saveWidgetData(
                                appWidgetId,
                                wallpaperUrl,
                                widgetType ?: "dday",
                                position
                            )
                            widgetPrefs.setTextColor(appWidgetId, textColor)
                            if (ddayTitle != null) {
                                widgetPrefs.setDdayTitle(appWidgetId, ddayTitle)
                            }
                            if (ddayDate != 0L) {
                                widgetPrefs.setDdayDate(appWidgetId, ddayDate)
                            }
                        } else {
                            return@launch
                        }
                    }


                    // appWidgetId에 해당하는 glanceId 가져오기
                    val glanceId = GlanceAppWidgetManager(context)
                        .getGlanceIdBy(appWidgetId)

                    try {
                        // 이미지 파일 캐시 확인 - 이미 있으면 다운로드 스킵
                        val cachedFile = File(context.cacheDir, "dday_wallpaper_${appWidgetId}.jpg")

                        // URL 변경 확인을 위한 해시 파일
                        val hashFile = File(context.cacheDir, "dday_wallpaper_${appWidgetId}.hash")
                        val currentHash = wallpaperUrl.hashCode().toString()
                        val cachedHash = if (hashFile.exists()) hashFile.readText() else ""

                        val imageFile = if (cachedFile.exists() && currentHash == cachedHash) {
                            cachedFile
                        } else {
                            if (cachedFile.exists()) {
                                cachedFile.delete()
                            } else {
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
                                this[WidgetKeys.DdayImageUrlKey] = imageFile.absolutePath
                            }
                        }

                        GoDdayWidgetProvider().update(context, glanceId)

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
            val cachedFile = File(context.cacheDir, "dday_wallpaper_${appWidgetId}.jpg")
            if (cachedFile.exists()) {
                cachedFile.delete()
            }

            // 해시 파일도 삭제
            val hashFile = File(context.cacheDir, "dday_wallpaper_${appWidgetId}.hash")
            if (hashFile.exists()) {
                hashFile.delete()
            }

        }
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
        val cacheFile = File(context.cacheDir, "dday_wallpaper_${widgetId}.jpg")

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
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)
        fos.flush()
        fos.close()

        // 리사이징한 비트맵이 원본과 다르면 메모리 해제
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }

        return cacheFile
    }

    companion object {
        private const val TAG = "GoDdayWidgetReceiver"
        const val ACTION_UPDATE_DDAY = "com.example.qwer_test_kt.UPDATE_DDAY"
        const val EXTRA_APP_WIDGET_ID = "target_app_widget_id"
    }
}
