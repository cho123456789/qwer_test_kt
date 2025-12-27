package com.example.qwer_test_kt.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwer_test_kt.domin.usecase.GetMainImagesByTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class PhotoWidgetViewModel @Inject constructor(
    private val getMainImagesByTypeUseCase: GetMainImagesByTypeUseCase
) : ViewModel() {

    private val _currentImage = MutableStateFlow<String?>(null)
    val currentImage: StateFlow<String?> = _currentImage.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>("디스코드")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // 초기 로드: 디스코드 테이블에서 데이터 가져오기
        loadImagesByType("디스코드")
    }

    // 특정 타입의 테이블에서 이미지 로드
    private fun loadImagesByType(typeName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val images = getMainImagesByTypeUseCase.invoke(typeName)
                // 이미지가 있으면 랜덤으로 선택
                if (images.isNotEmpty()) {
                    _currentImage.value = images.random().imageUrl
                }
            } catch (e: Exception) {
                android.util.Log.e("PhotoWidgetViewModel", "Error loading images for $typeName", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 카테고리 선택 시 해당 테이블에서 새로 데이터 로드
    fun selectRandomImage(category: String) {
        _selectedCategory.value = category
        loadImagesByType(category)
    }

    fun setWallpaper(
        context: Context,
        imageUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val contentUri = downloadAndSaveImage(context, imageUrl)

                // 배경화면 설정 Intent
                val intent = Intent(Intent.ACTION_SET_WALLPAPER).apply {
                    setDataAndType(contentUri, "image/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra("mimeType", "image/*")
                }

                withContext(Dispatchers.Main) {
                    try {
                        context.startActivity(intent)
                        onSuccess()
                    } catch (e: Exception) {
                        // ACTION_SET_WALLPAPER가 실패하면 대체 방법 사용
                        val chooserIntent = Intent.createChooser(
                            Intent(Intent.ACTION_ATTACH_DATA).apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                setDataAndType(contentUri, "image/*")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                putExtra("mimeType", "image/*")
                            },
                            "배경화면 설정"
                        )
                        context.startActivity(chooserIntent)
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("설정 실패: ${e.message}")
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun downloadAndSaveImage(context: Context, imageUrl: String): Uri {
        return withContext(Dispatchers.IO) {
            try {
                // 이미지 다운로드
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // 파일 저장
                val imagesDir = File(context.cacheDir, "images")
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }

                val imageFile = File(imagesDir, "wallpaper_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // FileProvider를 통해 Uri 생성
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    imageFile
                )
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
