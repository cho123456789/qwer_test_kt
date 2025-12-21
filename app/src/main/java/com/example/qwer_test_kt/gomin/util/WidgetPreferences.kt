package com.example.qwer_test_kt.gomin.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 위젯 관련 SharedPreferences 키 정의
 */
object WidgetPreferenceKeys {
    const val PREF_NAME = "WidgetData"
    const val KEY_WALLPAPER_URL = "widgetWallpaperUrl"
    const val KEY_WIDGET_TYPE = "widgetType"
    const val KEY_WIDGET_POSITION = "widgetPosition"
    const val KEY_TEXT_COLOR = "widgetTextColor"

    // 기본값
    const val DEFAULT_POSITION = "0.5,0.5,1.0"
    const val DEFAULT_TEXT_COLOR = "#FFFFFF" // 흰색
}

/**
 * 위젯 SharedPreferences 관리 클래스
 */
class WidgetPreferencesManager(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(WidgetPreferenceKeys.PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 배경 이미지 URL 가져오기
     */
    fun getWallpaperUrl(): String? {
        return prefs.getString(WidgetPreferenceKeys.KEY_WALLPAPER_URL, null)
    }

    /**
     * 배경 이미지 URL 저장
     */
    fun setWallpaperUrl(url: String) {
        prefs.edit { putString(WidgetPreferenceKeys.KEY_WALLPAPER_URL, url) }
    }

    /**
     * 위젯 타입 가져오기
     */
    fun getWidgetType(): String? {
        return prefs.getString(WidgetPreferenceKeys.KEY_WIDGET_TYPE, null)
    }

    /**
     * 위젯 타입 저장
     */
    fun setWidgetType(type: String) {
        prefs.edit { putString(WidgetPreferenceKeys.KEY_WIDGET_TYPE, type) }
    }

    /**
     * 위젯 위치 가져오기 (x,y,scale 형식)
     */
    fun getWidgetPosition(): String {
        return prefs.getString(
            WidgetPreferenceKeys.KEY_WIDGET_POSITION,
            WidgetPreferenceKeys.DEFAULT_POSITION
        ) ?: WidgetPreferenceKeys.DEFAULT_POSITION
    }

    /**
     * 위젯 위치 저장 (x,y,scale 형식)
     */
    fun setWidgetPosition(position: String) {
        prefs.edit { putString(WidgetPreferenceKeys.KEY_WIDGET_POSITION, position) }
    }

    /**
     * 텍스트 색상 가져오기 (hex 형식)
     */
    fun getTextColor(): String {
        return prefs.getString(
            WidgetPreferenceKeys.KEY_TEXT_COLOR,
            WidgetPreferenceKeys.DEFAULT_TEXT_COLOR
        ) ?: WidgetPreferenceKeys.DEFAULT_TEXT_COLOR
    }

    /**
     * 텍스트 색상 저장 (hex 형식)
     */
    fun setTextColor(colorHex: String) {
        prefs.edit { putString(WidgetPreferenceKeys.KEY_TEXT_COLOR, colorHex) }
    }

    /**
     * 모든 위젯 데이터 한번에 저장
     */
    fun saveWidgetData(wallpaperUrl: String, widgetType: String, position: String) {
        prefs.edit {
            putString(WidgetPreferenceKeys.KEY_WALLPAPER_URL, wallpaperUrl)
            putString(WidgetPreferenceKeys.KEY_WIDGET_TYPE, widgetType)
            putString(WidgetPreferenceKeys.KEY_WIDGET_POSITION, position)
        }
    }

    /**
     * 모든 위젯 데이터 삭제
     */
    fun clearAll() {
        prefs.edit { clear() }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: WidgetPreferencesManager? = null

        /**
         * 싱글톤 인스턴스 가져오기
         */
        fun getInstance(context: Context): WidgetPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: WidgetPreferencesManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
}
