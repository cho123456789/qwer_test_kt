package com.example.qwer_test_kt

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()

        // Firebase 초기화
        FirebaseApp.initializeApp(this)

        // Firebase App Check 초기화
        initializeAppCheck()

        // Firebase Analytics 초기화
        firebaseAnalytics = Firebase.analytics

        // Firebase Crashlytics는 자동으로 초기화됩니다
        // google-services.json 파일이 있으면 자동으로 크래시 리포트를 수집합니다

        // Firebase Analytics가 활성화되어 있는지 확인
        // 기본적으로 first_open, app_install 등의 이벤트는 자동으로 추적됩니다
    }

    private fun initializeAppCheck() {
        try {
            val firebaseAppCheck = FirebaseAppCheck.getInstance()

            if (BuildConfig.DEBUG) {
                // Debug 빌드: DebugAppCheckProviderFactory 사용
                // Firebase Console에서 Debug 토큰을 등록해야 합니다
                firebaseAppCheck.installAppCheckProviderFactory(
                    com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory.getInstance()
                )
            } else {
                // Release 빌드: Play Integrity API 사용
                firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
            }

        } catch (e: Exception) {
        }
    }
}