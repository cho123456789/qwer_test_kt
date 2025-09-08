package com.example.qwer_test_kt.data.source

import com.example.qwer_test_kt.data.model.MemberData

interface MemberRemoteDataSource {
    suspend fun getMemberDataList(): List<MemberData>
}