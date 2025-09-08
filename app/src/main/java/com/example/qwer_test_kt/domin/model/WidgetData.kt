package com.example.qwer_test_kt.domin.model

import android.graphics.Bitmap
import com.example.qwer_test_kt.data.model.MemberData

data class WidgetData(
    val wallpaperUrl: String? = null,
    val widgetType: String? = null,
    val wallpaperBitmap: Bitmap? = null
)
// 데이터 모델에서 받아온 json 데이터를 ui domin data로 매핑
fun MemberData.toMember(): Member {
    return Member(
        name = this.name,
        profileImageUrl = this.profileImageResId,
        wallpaperImageUrls = this.wallPaperImageUrls
    )
}