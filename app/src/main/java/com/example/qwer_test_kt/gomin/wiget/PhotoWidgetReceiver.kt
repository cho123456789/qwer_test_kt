package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
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

class PhotoWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = PhotoWidgetProvider()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // WidgetPreferencesManager 사용
        val widgetPrefs = WidgetPreferencesManager.getInstance(context)
        val wallpaperUrl = widgetPrefs.getWallpaperUrl() ?: ""

        if (wallpaperUrl.isEmpty()) {
            Log.e("PhotoWidgetReceiver", "배경화면 URL이 비어있습니다. 업데이트를 건너뜁니다.")
            return
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val glanceIds = GlanceAppWidgetManager(context)
                .getGlanceIds(PhotoWidgetProvider::class.java)

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
                    PhotoWidgetProvider().update(context, glanceId)

                } catch (e: Exception) {
                    Log.e("PhotoWidgetReceiver", "위젯 배경화면 업데이트 실패", e)
                }
            }
        }
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
        val tempFile =
            File(context.cacheDir, "photo_wallpaper_temp_${System.currentTimeMillis()}.png")
        val fos = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return tempFile
    }
}
