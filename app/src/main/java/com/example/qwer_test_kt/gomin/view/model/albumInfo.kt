package com.example.qwer_test_kt.gomin.view.model

import com.example.qwer_test_kt.R

data class albumInfo(
    val id: String? ,
    val title: Int,
    val content: Int,
    val date: Int,
    val image: Int,
    val trackList: List<Int>,
    val musicVideo: String,
    val cheerGuide: Int? = null // 응원법 이미지
)

val albums = listOf(
    albumInfo(
        "discord",
        R.string.discord_title,
        R.string.discord_content,
        R.string.discord_date,
        R.drawable.discord_title,
        listOf(R.string.discord_tickList_title, R.string.discord_tickList_song1, R.string.discord_tickList_song2),
        musicVideo = "https://www.youtube.com/watch?v=WGm2HmXeeRI",
        cheerGuide = R.drawable.discord_song
    ),
    albumInfo(
        "MANITO",
        R.string.MANITO_title,
        R.string.MANITO_content,
        R.string.MANITO_date,
        R.drawable.gomin_title,
        listOf(R.string.discord_tickList_title, R.string.MANITO_tickList_song1, R.string.MANITO_tickList_song2, R.string.MANITO_tickList_song3 , R.string.MANITO_tickList_song4, R.string.MANITO_tickList_song5, R.string.MANITO_tickList_song6),
        musicVideo = "https://www.youtube.com/watch?v=ImuWa3SJulY",
        cheerGuide = R.drawable.manito_song
    ),
    albumInfo("Algorithm",
        R.string.Algorithm_title,
        R.string.Algorithm_title,
        R.string.Algorithm_title,
        R.drawable.my_name_title,
        listOf(R.string.Algorithm_tickList_title, R.string.Algorithm_tickList_song1, R.string.Algorithm_tickList_song2,R.string.Algorithm_tickList_song3, R.string.Algorithm_tickList_song4, R.string.Algorithm_tickList_song5),
        musicVideo = "https://www.youtube.com/watch?v=AlirzLFEHUI",
        cheerGuide = R.drawable.algorithm_song
    ),
    albumInfo(
        "dear",
        R.string.dear_title,
        R.string.dear_content,
        R.string.dear_date,
        R.drawable.dear_title,
        listOf(R.string.dear_tickList_title, R.string.dear_tickList_song1, R.string.dear_tickList_song2, R.string.dear_tickList_song3, R.string.dear_tickList_song4, R.string.dear_tickList_song5),
        musicVideo = "https://www.youtube.com/watch?v=pifz9JH1Re8",
        cheerGuide = R.drawable.dear_song
    )
)