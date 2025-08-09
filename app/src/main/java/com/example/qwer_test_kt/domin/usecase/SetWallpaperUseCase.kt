package com.example.qwer_test_kt.domin.usecase

import android.graphics.Bitmap
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import javax.inject.Inject

// 가장 순수한 계층 / 플랫폼 독립성 ( 앱이 무엇을 하는지에 대한 비즈니스 로직)
class SetWallpaperUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) {
    suspend fun execute(bitmap: Bitmap, screenType: Int): Result<Unit> {
        return wallpaperRepository.setWallpaper(bitmap, screenType)
    }
}