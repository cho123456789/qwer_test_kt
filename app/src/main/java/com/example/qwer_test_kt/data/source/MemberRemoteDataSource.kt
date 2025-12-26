package com.example.qwer_test_kt.data.source

import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.MemberDetailData
import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.data.model.ProfileItemData

interface MemberRemoteDataSource {
    suspend fun getMemberMainImagesByType(typeName: String): List<MemberMainData>
    suspend fun getAllMemberMainImages(): List<MemberMainData>
    suspend fun getMemberDataList(): List<MemberData>
    suspend fun getProfileItemsByType(typeName: String): List<ProfileItemData>
    suspend fun getAllProfileItems(): List<ProfileItemData>
    suspend fun getMemberDetails(): List<MemberDetailData>
}