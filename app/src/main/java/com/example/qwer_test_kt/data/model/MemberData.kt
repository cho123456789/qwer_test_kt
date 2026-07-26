package com.example.qwer_test_kt.data.model

import com.example.qwer_test_kt.domin.model.MemberDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

// JSON 구조 동일한 데이터 클래스 정의 ( 서버에서 받아옴)
@Serializable
data class MemberData(
    val id: Long,
    @SerialName("member_name")
    val memberName: String,
    @SerialName("create_at")
    val createdAt: String? = null
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

@Serializable
data class MemberMainData(
    val id: Long,
    @SerialName("member_name")
    val memberName: String,
    @SerialName("image_url")
    val imageUrlJson: JsonElement,
) {
    /**
     * image_url is JSONB. Current rows store a one-item array such as
     * ["https://example.com/photo.jpg"], while older rows may contain a string.
     */
    val imageUrls: List<String>
        get() = when (val value = imageUrlJson) {
            is JsonPrimitive -> listOfNotNull(value.contentOrNull)
            is JsonArray -> value.mapNotNull { (it as? JsonPrimitive)?.contentOrNull }
            is JsonObject -> listOfNotNull(
                (value["image_url"] as? JsonPrimitive)?.contentOrNull,
                (value["url"] as? JsonPrimitive)?.contentOrNull
            )
            else -> emptyList()
        }

    val imageUrl: String?
        get() = imageUrls.firstOrNull()
}


// 멤버 상세 정보 데이터 모델 (Supabase qwer_member_detail_table용)
@Serializable
data class MemberDetailData(
    val id: Int,
    val name: String,
    val nickname: String,
    val birthday: String,
    val position: String,
    val mbti: String,
    @SerialName("profile_img")
    val profileImg: String
)

// MemberDetailData를 MemberDetail로 변환
fun MemberDetailData.toMemberDetail(): MemberDetail {
    return MemberDetail(
        id = this.id,
        name = this.name,
        birthday = this.birthday,
        nickname = this.nickname,
        position = this.position,
        mbti = this.mbti,
        profileImg = this.profileImg
    )
}
