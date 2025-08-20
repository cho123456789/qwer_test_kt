package com.example.qwer_test_kt.data.repository

import android.content.Context
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.repository.MemberRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okio.IOException
import javax.inject.Inject

// 데이터를 가져오는 로직 구현
class MemberRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MemberRepository {

    override suspend fun getMember(): List<Member> {
        return try {
            val jsonString =
                context.assets.open("member.json").bufferedReader().use { it.readText() }

            val memberDataList = Json.decodeFromString<List<Member>>(jsonString)

            memberDataList.map { data ->
                Member(
                    name = data.name,
                    profileImageResId = data.profileImageResId,
                    wallpaperImageUrls = data.wallpaperImageUrls
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}