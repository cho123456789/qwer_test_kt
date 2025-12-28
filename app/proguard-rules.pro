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