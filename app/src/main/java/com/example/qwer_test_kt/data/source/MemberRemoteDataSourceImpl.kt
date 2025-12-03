package com.example.qwer_test_kt.data.source

import android.util.Log
import com.example.qwer_test_kt.data.model.MemberData
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.Serializable
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val postgrest: Postgrest
) : MemberRemoteDataSource {
    override suspend fun getMemberDataList(): List<MemberData> {
        return try {
            val rows = postgrest.from("qwer_table")
                .select()
                .decodeList<MemberData>()

            Log.d("MemberRemoteDataSource", "Grouped into ${rows.size} members")
            rows

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching members", e)
            emptyList()
        }
    }
}