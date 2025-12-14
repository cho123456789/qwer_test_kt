package com.example.qwer_test_kt.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// JSON 구조 동일한 데이터 클래스 정의 ( 서버에서 받아옴)
@Serializable
data class MemberData(
    val id: Int,
    val name: String,
    val wallPaperImageUrls: List<String>
)

// 프로필 타입 테이블 (디스코드, 고민중독, 내이름맑음, 눈물참기)
@Serializable
data class ProfileTypeData(
    val id: Int,
    @SerialName("type_name")
    val typeName: String
)

// 프로필 아이템 테이블 (멤버별 이미지)
@Serializable
data class ProfileItemData(
    val id: Int,
    @SerialName("type_name")
    val typeName: String,
    @SerialName("item_name")
    val memberName: String,
    @SerialName("item_url")
    val imageUrl: String
)

// 조인된 프로필 데이터 (타입별로 그룹화)
@Serializable
data class ProfileByTypeData(
    val typeName: String,
    val members: List<MemberProfileData>
)

// 멤버별 프로필 데이터
@Serializable
data class MemberProfileData(
    val memberName: String,
    val imageUrl: String
)