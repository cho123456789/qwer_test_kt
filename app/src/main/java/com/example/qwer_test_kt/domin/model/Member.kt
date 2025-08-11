package com.example.qwer_test_kt.domin.model

// 앱의 비즈니스 로직에서 사용될 핵심 데이터 모델
data class Member(
    val name: String,
    val profileImageUrl: String,
    val wallPaperImageResId: List<String>
)