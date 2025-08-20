package com.example.qwer_test_kt.domin.model


// UI에 표시할 데이터 모델

data class Member(
    val name: String,
    val profileImageUrl: String,
    val wallpaperImageUrls: List<String>
)