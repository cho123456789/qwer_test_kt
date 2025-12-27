package com.example.qwer_test_kt.data.source

import android.util.Log
import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.MemberDetailData
import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.data.model.ProfileItemData
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val postgrest: Postgrest
) : MemberRemoteDataSource {

    // 타입별로 해당 테이블에서 데이터 가져오기
    override suspend fun getMemberMainImagesByType(typeName: String): List<MemberMainData> {
        return try {
            // 타입에 따라 다른 테이블에서 조회
            val tableName = when (typeName) {
                "디스코드" -> "qwer_discord_table"
                "고민중독" -> "qwer_gomin_table"
                "내이름맑음" -> "qwer_myname_table"
                "눈물참기" -> "qwer_dear_table"
                else -> {
                    Log.w("MemberRemoteDataSource", "Unknown type: $typeName, using default table")
                    "qwer_image_table"
                }
            }

            val rows = postgrest.from(tableName)
                .select()
                .decodeList<MemberMainData>()

            Log.d(
                "MemberRemoteDataSource",
                "Fetched ${rows.size} images from $tableName for type: $typeName"
            )
            rows

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching images for type: $typeName", e)
            emptyList()
        }
    }

    // 모든 테이블에서 데이터 가져오기
    override suspend fun getAllMemberMainImages(): List<MemberMainData> {
        return try {
            val allImages = mutableListOf<MemberMainData>()

            // 모든 테이블에서 데이터 수집
            val tables = listOf(
                "qwer_discord_table",
                "qwer_gomin_table",
                "qwer_myname_table",
                "qwer_dear_table"
            )

            tables.forEach { tableName ->
                try {
                    val rows = postgrest.from(tableName)
                        .select()
                        .decodeList<MemberMainData>()
                    allImages.addAll(rows)
                    Log.d("MemberRemoteDataSource", "Fetched ${rows.size} images from $tableName")
                } catch (e: Exception) {
                    Log.e("MemberRemoteDataSource", "Error fetching from $tableName", e)
                }
            }

            Log.d(
                "MemberRemoteDataSource",
                "Fetched ${allImages.size} total main images from all tables"
            )
            allImages

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching all main images", e)
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

    override suspend fun getMemberDetails(): List<MemberDetailData> {
        return try {
            val rows = postgrest.from("qwer_profile_info")
                .select()
                .decodeList<MemberDetailData>()

            Log.d("MemberRemoteDataSource", "Fetched ${rows.size} member details")
            rows.forEach {
                Log.d(
                    "MemberRemoteDataSource",
                    "Member: ${it.name}, Position: ${it.position}, MBTI: ${it.mbti}"
                )
            }
            rows

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching member details: ${e.message}", e)
            e.printStackTrace()
            emptyList()
        }
    }
}