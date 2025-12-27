package com.example.qwer_test_kt.data.repository

import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.data.model.toMemberDetail
import com.example.qwer_test_kt.data.source.MemberRemoteDataSource
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.MemberDetail
import com.example.qwer_test_kt.domin.model.ProfileByType
import com.example.qwer_test_kt.domin.model.toMember
import com.example.qwer_test_kt.domin.model.toProfileByTypeList
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 데이터를 변환
class MemberRepositoryImpl @Inject constructor(
    private val remoteDataSource: MemberRemoteDataSource
) : MemberRepository {
    override suspend fun getMainImage(): List<MemberMainData> {
        return remoteDataSource.getAllMemberMainImages()
    }

    override suspend fun getMainImagesByType(typeName: String): List<MemberMainData> {
        return remoteDataSource.getMemberMainImagesByType(typeName)
    }

    override suspend fun getProfilesByType(typeName: String): ProfileByType? {
        val profileItems = remoteDataSource.getProfileItemsByType(typeName)
        return profileItems.toProfileByTypeList().firstOrNull()
    }

    override suspend fun getAllProfiles(): List<ProfileByType> {
        val allProfileItems = remoteDataSource.getAllProfileItems()
        return allProfileItems.toProfileByTypeList()
    }

    override suspend fun getMemberDetails(): List<MemberDetail> {
        val memberDetailDataList = remoteDataSource.getMemberDetails()
        return memberDetailDataList.map { it.toMemberDetail() }
    }
}