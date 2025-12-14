package com.example.qwer_test_kt.data.source

import android.util.Log
import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.ProfileItemData
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
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

    override suspend fun getProfileItemsByType(typeName: String): List<ProfileItemData> {
        return try {
            val rows = postgrest.from("qwer_profile_item_table")
                .select()
                .decodeList<ProfileItemData>()
                .filter { it.typeName == typeName }

            Log.d(
                "MemberRemoteDataSource",
                "Fetched ${rows.size} profile items for type: $typeName"
            )
            rows

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching profile items by type", e)
            emptyList()
        }
    }

    override suspend fun getAllProfileItems(): List<ProfileItemData> {
        return try {
            val rows = postgrest.from("qwer_profile_item_table")
                .select()
                .decodeList<ProfileItemData>()

            Log.d("MemberRemoteDataSource", "Fetched ${rows.size} total profile items")
            rows

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching all profile items", e)
            emptyList()
        }
    }
}