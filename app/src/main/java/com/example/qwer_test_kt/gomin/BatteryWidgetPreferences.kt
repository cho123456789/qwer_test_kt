package com.example.qwer_test_kt.gomin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.state.GlanceStateDefinition
import java.io.File

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "widget_data_store")

// 올바른 GlanceStateDefinition 구현
object BatteryWidgetStateDefinition : GlanceStateDefinition<Preferences> {

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<Preferences> {
        return context.dataStore
    }

    override fun getLocation(
        context: Context,
        fileKey: String
    ): File {
        return File(context.filesDir, "datastore/preferences_pb_$fileKey")
    }
}