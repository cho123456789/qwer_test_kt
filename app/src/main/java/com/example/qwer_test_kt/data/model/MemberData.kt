package com.example.qwer_test_kt.data.model

import com.example.qwer_test_kt.domin.model.Member
import kotlinx.serialization.Serializable

// JSON 구조 동일한 데이터 클래스 정의 ( 서버에서 받아옴)
@Serializable
data class MemberData(
    val name: String,
    val wallPaperImageUrls: List<String>
)