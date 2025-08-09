package com.example.qwer_test_kt.data

import android.app.Application
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val application: Application
) : WallpaperRepository {
    override suspend fun setWallpaper(bitmap: Bitmap, screenType: Int): Result<Unit> {
        return try {
            val wallpaperManager = WallpaperManager.getInstance(application)
            wallpaperManager.setBitmap(bitmap, null, true, screenType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWallpaperBitmap(url: String): Result<Bitmap> {
        return try {
            val imageLoader = ImageLoader(application)
            val request = ImageRequest.Builder(application)
                .data(url)
                .allowHardware(false)
                .build()
            val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
            val bitmap = (result as BitmapDrawable).bitmap
            if (bitmap != null) {
                Result.success(bitmap)
            } else {
                Result.failure(Exception("Bitmap is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}