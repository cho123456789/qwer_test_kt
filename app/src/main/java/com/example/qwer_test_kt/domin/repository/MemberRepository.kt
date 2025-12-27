package com.example.qwer_test_kt.domin.repository

import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.MemberDetail
import com.example.qwer_test_kt.domin.model.ProfileByType

// 데이터 계층 통신 하기위한 인터페이스 정의
interface MemberRepository {
    suspend fun getMainImage() : List<MemberMainData>
    suspend fun getMainImagesByType(typeName: String): List<MemberMainData>
    suspend fun getProfilesByType(typeName: String): ProfileByType?
    suspend fun getAllProfiles(): List<ProfileByType>
    suspend fun getMemberDetails(): List<MemberDetail>
}