package com.example.qwer_test_kt.data.model

import kotlinx.serialization.Serializable

// JSON 구조 동일한 데이터 클래스 정의 ( 서버에서 받아옴)
@Serializable
data class MemberData(
    val name: String,
    val profileImageResId: String,
    val wallpaperImageUrls: List<String>
)

@Serializable
data class MemberList(
    val members: List<MemberData>
)