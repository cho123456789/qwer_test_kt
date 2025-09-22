package com.example.qwer_test_kt.gomin.view.model

import com.example.qwer_test_kt.R

data class MemberInfo(
    val name: String,
    val position: String,
    val image: Int,
    val description: String
)

val members = listOf(
    MemberInfo(
        "쵸단",
        "드럼",
        R.drawable.gomin_cho_profile,
        "QWER의 드러머이자 리더입니다."
    ),
    MemberInfo(
        "마젠타",
        "베이스",
        R.drawable.gomin_ma_profile,
        "매력적인 비주얼과 안정적인 베이스 연주를 담당합니다."
    ),
    MemberInfo(
        "히나",
        "기타",
        R.drawable.gomin_hina_profile,
        "청량한 기타 사운드를 책임집니다."
    ),
    MemberInfo(
        "시연",
        "리드 보컬",
        R.drawable.gomin_siyeon_profile,
        "뛰어난 가창력으로 밴드의 메인 보컬을 맡고 있습니다."
    )
)