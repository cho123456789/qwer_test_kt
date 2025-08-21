package com.example.qwer_test_kt.data.model

import com.example.qwer_test_kt.domin.model.Member
import kotlinx.serialization.Serializable

// JSON 구조 동일한 데이터 클래스 정의 ( 서버에서 받아옴)
@Serializable
data class MemberData(
    val name: String,
    val profileImageResId: String,
    val wallPaperImageUrls: List<String>
)

// 데이터 모델에서 받아온 json 데이터를 ui domin data로 매핑
fun MemberData.toMember(): Member {
    return Member(
        name = this.name,
        profileImageUrl = this.profileImageResId,
        wallpaperImageUrls = this.wallPaperImageUrls
    )
}