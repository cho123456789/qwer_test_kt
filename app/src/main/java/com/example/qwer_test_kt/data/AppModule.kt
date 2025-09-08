package com.example.qwer_test_kt.data

import androidx.glance.appwidget.GlanceAppWidget
import com.example.qwer_test_kt.gomin.wiget.BatteryGlanceWidgetProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGlanceAppWidget(): GlanceAppWidget {
        return BatteryGlanceWidgetProvider()
    }
}