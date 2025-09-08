package com.example.qwer_test_kt.data.source

import android.content.Context
import com.example.qwer_test_kt.data.model.MemberData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MemberRemoteDataSource {

    override suspend fun getMemberDataList(): List<MemberData> {
        val jsonString: String
        try {
            jsonString = context.assets.open("member.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }
        return Json.decodeFromString<List<MemberData>>(jsonString)
    }
}