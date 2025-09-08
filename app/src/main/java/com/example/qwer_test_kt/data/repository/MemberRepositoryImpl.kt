package com.example.qwer_test_kt.data.repository

import com.example.qwer_test_kt.data.source.MemberRemoteDataSource
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.toMember
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 데이터를 변환
class MemberRepositoryImpl @Inject constructor(
    private val remoteDataSource: MemberRemoteDataSource
) : MemberRepository {

    override suspend fun getMember(): List<Member> {
        val memberDataList = remoteDataSource.getMemberDataList()
        return memberDataList.map { it.toMember() }
    }
}