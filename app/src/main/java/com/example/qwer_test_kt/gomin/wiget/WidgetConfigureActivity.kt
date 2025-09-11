package com.example.qwer_test_kt.gomin.wiget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.qwer_test_kt.R
import com.example.qwer_test_kt.presentation.BatteryWidgetViewmodel
import com.example.qwer_test_kt.presentation.downloadBitmap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WidgetConfigureActivity : ComponentActivity() {
    private val viewModel: BatteryWidgetViewmodel by viewModels()

    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)

        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
        val sharedPrefs = this.getSharedPreferences("WidgetData", Context.MODE_PRIVATE)
        val wallpaperUrl = sharedPrefs.getString("widgetWallpaperUrl", null)
        val widgetType = sharedPrefs.getString("widgetType", null)


        if (widgetType != null && wallpaperUrl != null) {
            lifecycleScope.launch {
                val bitmap = downloadBitmap(this@WidgetConfigureActivity, wallpaperUrl)
                if (bitmap != null) {
                    // 2. Pass the bitmap (not the URL) to the preview function
                    updateWidgetPreview(bitmap)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun updateWidgetPreview(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val componentName = ComponentName(this, GoBatteryWidgetProvider::class.java)
            val previewViews = RemoteViews(packageName, R.layout.battery_go_widget)

            previewViews.setImageViewBitmap(R.id.widget_background, bitmap)

            // 4. 올바른 위젯 카테고리 상수 사용
            appWidgetManager.setWidgetPreview(
                componentName,
                AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN,
                previewViews
            )
        }
    }
}