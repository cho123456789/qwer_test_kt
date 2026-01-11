# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ============================================
# Supabase 키 보호 - ProGuard 난독화
# ============================================

# BuildConfig 클래스의 민감한 정보 난독화
# Release 빌드 시 SUPABASE_URL, SUPABASE_KEY 필드명이 a, b, c 등으로 변경됨
-assumenosideeffects class com.example.qwer_test_kt.BuildConfig {
    public static java.lang.String SUPABASE_URL;
    public static java.lang.String SUPABASE_KEY;
}

# 클래스명, 패키지명, 필드명 난독화 강화
-repackageclasses ''
-allowaccessmodification
-optimizationpasses 5

# ============================================
# Supabase 라이브러리 유지 규칙
# ============================================

# Supabase 클라이언트 관련 클래스 유지
-keep class io.github.jan.supabase.** { *; }
-keep interface io.github.jan.supabase.** { *; }

# Ktor 관련 클래스 유지 (Supabase가 사용)
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }

# ============================================
# Kotlinx Serialization 유지
# ============================================

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.example.qwer_test_kt.**$$serializer { *; }
-keepclassmembers class com.example.qwer_test_kt.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.qwer_test_kt.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ============================================
# Hilt 관련 유지 규칙
# ============================================

-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ============================================
# Compose 관련 유지 규칙
# ============================================

-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# ============================================
# 데이터 모델 유지 (Serialization 필요)
# ============================================

-keep class com.example.qwer_test_kt.data.model.** { *; }

# ============================================
# Coroutines 유지
# ============================================

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ============================================
# 일반 최적화 규칙
# ============================================

# 크래시 리포트를 위한 라인 번호 유지
-keepattributes SourceFile,LineNumberTable

# 제네릭 타입 정보 유지
-keepattributes Signature

# 예외 정보 유지
-keepattributes Exceptions

# ============================================
# Firebase 관련 유지 규칙
# ============================================

# Firebase 핵심 클래스 유지
-keep class com.google.firebase.** { *; }
-keep interface com.google.firebase.** { *; }

# Firebase Analytics
-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.android.gms.analytics.** { *; }

# Firebase google-services.json의 API 키는 클라이언트 측 키입니다
# 이 키 자체는 공개되어도 괜찮으며, Firebase Console의 Security Rules로 보안을 관리해야 합니다
# Analytics는 읽기 전용이므로 API 키만으로는 데이터 조작이 불가능합니다

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable

# Crashlytics는 난독화된 코드의 스택 트레이스를 자동으로 매핑합니다
# mapping.txt 파일이 자동으로 업로드됩니다

# Firebase App Check
-keep class com.google.firebase.appcheck.** { *; }
-keep interface com.google.firebase.appcheck.** { *; }

# Play Integrity API
-keep class com.google.android.play.core.integrity.** { *; }

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Keep Firebase annotations
-keepattributes *Annotation*

# ============================================
# R8 Missing Classes 경고 제거 (자동 생성됨)
# ============================================

# Firebase KTX 확장 함수 관련 (컴파일 타임에만 필요)
-dontwarn com.google.firebase.ktx.Firebase
-dontwarn com.google.firebase.ktx.FirebaseKt

# Java Management (일부 라이브러리가 참조하지만 Android에서는 불필요)
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean

# SLF4J 로깅 (Android는 자체 로깅 사용)
-dontwarn org.slf4j.impl.StaticLoggerBinder