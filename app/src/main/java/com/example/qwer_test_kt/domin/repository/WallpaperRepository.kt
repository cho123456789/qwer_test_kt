package com.example.qwer_test_kt.domin.repository

import android.graphics.Bitmap

// 필요한 기능 요청
interface WallpaperRepository {
    suspend fun setWallpaper(bitmap: Bitmap, screenType : Int) : Result<Unit>
    suspend fun getWallpaperBitmap(url : String) : Result<Bitmap>
}