package com.example.qwer_test_kt.data.repository

import android.content.Context
import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.toMember
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
        val jsonString: String
        try {
            jsonString = context.assets.open("member.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val memberDataList = Json.decodeFromString<List<MemberData>>(jsonString)
        return memberDataList.map { it.toMember() }
    }

}