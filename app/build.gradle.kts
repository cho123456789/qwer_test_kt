import java.util.Properties

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.example.qwer_test_kt"
    compileSdk = 35

    defaultConfig {

        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${properties.getProperty("supabase.url") ?: ""}\""
        )
        buildConfigField(
            "String",
            "SUPABASE_KEY",
            "\"${properties.getProperty("supabase.key") ?: ""}\""
        )

        applicationId = "com.example.qwer_test_kt"
        minSdk = 24
        targetSdk = 35
        versionCode = 1 
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose =  true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {

    implementation("io.github.jan-tennert.supabase:supabase-kt:2.6.1")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.6.1")
    implementation("io.ktor:ktor-client-android:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.glance:glance-appwidget:1.1.1")
    implementation("androidx.glance:glance-appwidget-proto:1.1.1")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.coil.compose)
    implementation (libs.accompanist.pager)
    implementation(libs.androidx.material3.android)
    val compose_bom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(compose_bom)
    androidTestImplementation(compose_bom)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
    implementation (libs.androidx.activity.compose)
    implementation (libs.androidx.ui)
    implementation (libs.androidx.material)
    implementation (libs.androidx.ui.tooling.preview)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}