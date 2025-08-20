package com.example.qwer_test_kt.domin.repository

import android.graphics.Bitmap
import com.example.qwer_test_kt.domin.model.Member

// 데이터 계층 통신 하기위한 인터페이스 정의
interface MemberRepository {
    suspend fun getMember() : List<Member>
}