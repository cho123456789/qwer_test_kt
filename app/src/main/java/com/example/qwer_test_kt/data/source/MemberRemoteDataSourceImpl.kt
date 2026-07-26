package com.example.qwer_test_kt.data.source

import com.example.qwer_test_kt.data.model.MemberDetailData
import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.data.model.ProfileItemData
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val postgrest: Postgrest
) : MemberRemoteDataSource {

    private val memberImageTables = mapOf(
        "JENA" to "jena_images",
        "LIV" to "liv_images",
        "MEI" to "mei_images",
        "MINAMI" to "minami_images",
        "WONI" to "woni_images"
    )

    override suspend fun getMembers(): List<MemberData> = try {
        postgrest.from("members").select().decodeList<MemberData>()
    } catch (_: Exception) {
        emptyList()
    }

    override suspend fun getMemberMainImagesByType(typeName: String): List<MemberMainData> = try {
        memberImageTables[typeName.uppercase()]?.let { tableName ->
            postgrest.from(tableName).select {
                filter {
                    eq("member_name", typeName.uppercase())
                }
            }.decodeList<MemberMainData>()
        } ?: emptyList()
    } catch (_: Exception) {
        emptyList()
    }

    override suspend fun getAllMemberMainImages(): List<MemberMainData> = try {
        memberImageTables.values.flatMap { tableName ->
            try {
                postgrest.from(tableName).select().decodeList<MemberMainData>()
            } catch (_: Exception) {
                emptyList()
            }
        }
    } catch (_: Exception) {
        emptyList()
    }

    override suspend fun getProfileItemsByType(typeName: String): List<ProfileItemData> = try {
        postgrest.from("qwer_profile_item_table").select().decodeList<ProfileItemData>()
            .filter { it.typeName == typeName }
    } catch (_: Exception) { emptyList() }

    override suspend fun getAllProfileItems(): List<ProfileItemData> = try {
        postgrest.from("qwer_profile_item_table").select().decodeList<ProfileItemData>()
    } catch (_: Exception) { emptyList() }

    override suspend fun getMemberDetails(): List<MemberDetailData> = try {
        postgrest.from("qwer_profile_info").select().decodeList<MemberDetailData>()
    } catch (_: Exception) { emptyList() }
}
