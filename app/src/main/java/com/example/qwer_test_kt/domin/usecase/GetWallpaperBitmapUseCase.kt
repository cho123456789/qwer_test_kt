package com.example.qwer_test_kt.domin.usecase

import android.graphics.Bitmap
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import javax.inject.Inject

class GetWallpaperBitmapUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend fun execute(url: String): Result<Bitmap> {
        return wallpaperRepository.getWallpaperBitmap(url)
    }
}