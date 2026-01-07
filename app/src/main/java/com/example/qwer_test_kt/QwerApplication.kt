package com.example.qwer_test_kt

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class qwerApplication : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private const val TAG = "QwerApplication"
    }

    override fun onCreate() {
        super.onCreate()

        // Firebase 초기화
        FirebaseApp.initializeApp(this)
        Log.d(TAG, "Firebase initialized")

        // Firebase App Check 초기화
        initializeAppCheck()

        // Firebase Analytics 초기화
        firebaseAnalytics = Firebase.analytics
        Log.d(TAG, "Firebase Analytics initialized")

        // Firebase Crashlytics는 자동으로 초기화됩니다
        // google-services.json 파일이 있으면 자동으로 크래시 리포트를 수집합니다

        // Firebase Analytics가 활성화되어 있는지 확인
        // 기본적으로 first_open, app_install 등의 이벤트는 자동으로 추적됩니다
    }

    private fun initializeAppCheck() {
        try {
            val firebaseAppCheck = FirebaseAppCheck.getInstance()

            Log.d(TAG, "=== Firebase App Check 설정 완료 ===")
            Log.d(TAG, "Debug 모드: ${BuildConfig.DEBUG}")

            if (BuildConfig.DEBUG) {
                // Debug 빌드: DebugAppCheckProviderFactory 사용
                // Firebase Console에서 Debug 토큰을 등록해야 합니다
                firebaseAppCheck.installAppCheckProviderFactory(
                    com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "App Check Provider: DebugAppCheckProviderFactory")
                Log.d(TAG, "Logcat에서 Debug Token을 확인하세요:")
                Log.d(TAG, "필터: 'DebugAppCheckProvider' 또는 'FirebaseAppCheck'")
            } else {
                // Release 빌드: Play Integrity API 사용
                firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
                Log.d(TAG, "App Check Provider: PlayIntegrityAppCheckProviderFactory")
                Log.d(TAG, "Play Integrity API가 자동으로 앱을 검증합니다")
            }

        } catch (e: Exception) {
            Log.e(TAG, "App Check 초기화 실패: ${e.message}", e)
        }
    }
}