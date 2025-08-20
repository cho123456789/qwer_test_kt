package com.example.qwer_test_kt.domin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// UI에 표시할 데이터 모델

@Serializable
data class Member(
    val name : String,
    @SerialName("profileImageResId")
    val profileImageResId : String,
    @SerialName("wallPaperImageUrls")
    val wallpaperImageUrls : List<String>
)