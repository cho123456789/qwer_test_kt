package com.example.qwer_test_kt

import com.example.qwer_test_kt.presentation.BatteryWidgetViewmodel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getBatteryWidgetViewModel(): BatteryWidgetViewmodel
}