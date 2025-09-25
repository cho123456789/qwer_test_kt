package com.example.qwer_test_kt.gomin.view.model

import com.example.qwer_test_kt.R

data class MemberInfo(
    val name: String,
    val position: String,
    val nickname: String,
    val birthday: String,
    val image: Int,
    val description: String
)

val members = listOf(
    MemberInfo(
        "홍지혜",
        "드럼",
         "쵸단",
        "1998.11.01",
        R.drawable.gomin_cho_profile2,
        "QWER의 드러머이자 리더입니다. 🥁"
    ),
    MemberInfo(
        "이아희",
        "베이스",
        "마젠타",
        "1997.06.02",
        R.drawable.gomin_ma_profile2,
        "베이스 연주를 담당합니다. 🎸"
    ),
    MemberInfo(
        "장나영",
        "기타",
        nickname = "히나",
        "2001.01.30",
        R.drawable.gomin_hina_profile2,
        "청량한 기타 사운드를 책임집니다. 🎸"
    ),
    MemberInfo(
        "이시연",
        "리드 보컬",
          "이시연",
        "2000.05.16",
        R.drawable.gomin_siyeon_profile2,
        "밴드의 메인 보컬을 맡고 있습니다. 🎤"
    )
)