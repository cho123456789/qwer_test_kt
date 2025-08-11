package com.example.qwer_test_kt.data

import com.google.firebase.firestore.PropertyName

/// Firestore의 데이터 구조를 반영하는 DTO 클래스를 정의
/**
 * Firebase Firestore에서 가져오는 데이터의 구조를 나타내는 데이터 전송 객체(DTO).
 * Firestore 문서의 필드명과 일치해야 합니다.
 */
data class MemberDto(
    @PropertyName("name")
    val name: String = "",
    @PropertyName("profileImage")
    val profileImage: String = "",
    @PropertyName("wallPaperImageResId")
    val wallPaperImageResId: List<String> = emptyList()
)