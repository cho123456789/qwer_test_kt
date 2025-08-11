package com.example.qwer_test_kt.domin.repository

import com.example.qwer_test_kt.domin.model.Member

// 파일에 데이터 접근에 대한 추상적인 계약 정의
// 인터페이스는 도메인 레이어가 데이터 레이어와 상호작용하는 유일한 창구
interface MemberRepository {
    suspend fun getMembers(): Result<List<Member>>
}