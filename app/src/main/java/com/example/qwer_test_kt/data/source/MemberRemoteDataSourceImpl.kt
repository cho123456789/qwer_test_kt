package com.example.qwer_test_kt.data.source

import android.util.Log
import com.example.qwer_test_kt.data.model.MemberData
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.Serializable
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val postgrest: Postgrest
) : MemberRemoteDataSource {

    // Supabase에서 가져온 raw 데이터 (각 row는 하나의 이미지)
    @Serializable
    private data class MemberRow(
        val id: Int,
        val name: String,
        val wallPaperImageUrls: String
    )

    override suspend fun getMemberDataList(): List<MemberData> {
        return try {
            // Supabase 테이블에서 모든 데이터 가져오기
            val rows = postgrest.from("qwer_table")
                .select()
                .decodeList<MemberRow>()

            Log.d("MemberRemoteDataSource", "Successfully fetched ${rows.size} rows")

            // 같은 이름의 멤버들을 그룹화하고 이미지 URL들을 리스트로 합치기
            val groupedMembers = rows
                .groupBy { it.name }
                .map { (name, memberRows) ->
                    MemberData(
                        name = name,
                        wallPaperImageUrls = memberRows.map { it.wallPaperImageUrls }
                    )
                }

            Log.d("MemberRemoteDataSource", "Grouped into ${groupedMembers.size} members")
            groupedMembers

        } catch (e: Exception) {
            Log.e("MemberRemoteDataSource", "Error fetching members", e)
            emptyList()
        }
    }
}