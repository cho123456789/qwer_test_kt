package com.example.qwer_test_kt.domin.model

import com.example.qwer_test_kt.data.model.MemberData


// UI에 표시할 데이터 모델
data class Member(
    val name: String,
    val wallpaperImageUrls: List<String>
)
// 데이터 모델에서 받아온 json 데이터를 ui domin data로 매핑
fun MemberData.toMember(): Member {
    return Member(
        name = this.name,
        wallpaperImageUrls = this.wallPaperImageUrls
    )
}