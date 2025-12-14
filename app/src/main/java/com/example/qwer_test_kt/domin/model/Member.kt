package com.example.qwer_test_kt.domin.model

import com.example.qwer_test_kt.data.model.MemberData
import com.example.qwer_test_kt.data.model.ProfileItemData


// UI에 표시할 데이터 모델
data class Member(
    val name: String,
    val wallpaperImageUrls: List<String>
)

// 멤버 상세 정보 데이터 모델
data class MemberDetail(
    val id: Int,
    val name: String,
    val birthday: String,
    val nickname: String,
    val position: String,
    val mbti: String,
    val profileImg: String
)

// 프로필 타입별 멤버 데이터 (UI용)
data class ProfileByType(
    val typeName: String,
    val members: Map<String, String> // memberName to imageUrl
)

// 데이터 모델에서 받아온 json 데이터를 ui domin data로 매핑
fun MemberData.toMember(): Member {
    return Member(
        name = this.name,
        wallpaperImageUrls = this.wallPaperImageUrls
    )
}

// ProfileItemData 리스트를 타입별로 그룹화하여 ProfileByType으로 변환
fun List<ProfileItemData>.toProfileByTypeList(): List<ProfileByType> {
    return this.groupBy { it.typeName }
        .map { (typeName, items) ->
            ProfileByType(
                typeName = typeName,
                members = items.associate { it.memberName to it.imageUrl }
            )
        }
}