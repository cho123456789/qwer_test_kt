package com.example.qwer_test_kt.gomin.view.model

import com.example.qwer_test_kt.R

data class albumInfo(
    val title: String,
    val content: String,
    val date: String,
    val image: Int,
    val trackList: List<String>
)

val albums = listOf(
    albumInfo(
        "Discord",
        "The 1st Single Album",
        "2023.10.18",
        R.drawable.discord_title,
        listOf("Discord (title)", "별의 하모니", "수수께끼 다이어리")
    ),
    albumInfo(
        "MANITO",
        "The 1st Mini Album",
        "2024.04.01",
        R.drawable.gomin_title,
        listOf("고민중독 (title)", "SODA", "자유선언", "지구정복", "대관람차", "불꽃놀이", "마니또")
    ),
    albumInfo(
        "Algorithm’s Blossom",
        "The 2st Mini Album",
        "2024.09.23",
        R.drawable.my_name_title,
        listOf("내이름 맑음 (title)", "가짜아이돌", "사랑하자", "달리기", "안녕, 나의 슬픔", "메아리")
    ),
    albumInfo(
        "난 네 편이야, 온 세상이 불협일지라도",
        "The 3st Mini Album",
        "2025.05.06",
        R.drawable.dear_title,
        listOf("눈물참기 (title)", "행복해져라", "검색어는 QWER", "OVERDRIVE", "D-Day", "Yours Sincerely")
    )
)