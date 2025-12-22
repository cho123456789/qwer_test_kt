package com.example.qwer_test_kt.presentation

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwer_test_kt.domin.model.ProfileByType
import com.example.qwer_test_kt.domin.usecase.GetAllProfilesUseCase
import com.example.qwer_test_kt.gomin.util.WidgetPreferencesManager
import com.example.qwer_test_kt.gomin.wiget.GoWatchWidgetReceiver
import com.example.qwer_test_kt.gomin.wiget.PhotoWidgetReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val getAllProfilesUseCase: GetAllProfilesUseCase
) : ViewModel() {

    private val _profiles = MutableStateFlow<List<ProfileByType>?>(null)
    val profiles: StateFlow<List<ProfileByType>?> = _profiles.asStateFlow()

    private val _currentImage = MutableStateFlow<String?>(null)
    val currentImage: StateFlow<String?> = _currentImage.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String>("디스코드")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _imageScale = MutableStateFlow(1f)
    val imageScale: StateFlow<Float> = _imageScale.asStateFlow()

    init {
        viewModelScope.launch {
            val result = getAllProfilesUseCase.invoke()
            _profiles.value = result
            // 프로필 데이터가 로드된 후 초기 이미지 설정
            if (result != null && result.isNotEmpty()) {
                selectRandomImage("디스코드")
            }
        }
    }

    fun selectRandomImage(category: String) {
        _selectedCategory.value = category
        val profile = _profiles.value?.find { it.typeName == category }
        profile?.let {
            val images = it.members.values.toList()
            if (images.isNotEmpty()) {
                _currentImage.value = images.random()
                // 이미지 선택 시 애니메이션 실행
                playImageAnimation()
            }
        }
    }

    private fun playImageAnimation() {
        viewModelScope.launch {
            _imageScale.value = 0.9f
            delay(100)
            _imageScale.value = 1.1f
            delay(100)
            _imageScale.value = 1f
        }
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

    fun registerPhotoWidget(
        context: Context,
        imageUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)

                    if (!appWidgetManager.isRequestPinAppWidgetSupported) {
                        withContext(Dispatchers.Main) {
                            onError("이 런처는 위젯 고정을 지원하지 않습니다.")
                        }
                        return@launch
                    }

                    // WidgetPreferencesManager 사용
                    val widgetPrefs = WidgetPreferencesManager.getInstance(context)
                    widgetPrefs.setWallpaperUrl(imageUrl)
                    widgetPrefs.setWidgetType("photo")

                    // 사진 위젯 등록
                    val providerComponent = ComponentName(context, PhotoWidgetReceiver::class.java)

                    withContext(Dispatchers.Main) {
                        val success =
                            appWidgetManager.requestPinAppWidget(providerComponent, null, null)

                        if (success) {
                            onSuccess()
                        } else {
                            onError("위젯 추가를 취소했거나 실패했습니다.")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("위젯 고정은 Android 8.0 이상에서 지원됩니다.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("위젯 등록 실패: ${e.message}")
                }
            }
        }
    }

    fun registerClockWidget(
        context: Context,
        imageUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)

                    if (!appWidgetManager.isRequestPinAppWidgetSupported) {
                        withContext(Dispatchers.Main) {
                            onError("이 런처는 위젯 고정을 지원하지 않습니다.")
                        }
                        return@launch
                    }

                    // WidgetPreferencesManager 사용
                    val widgetPrefs = WidgetPreferencesManager.getInstance(context)
                    widgetPrefs.setWallpaperUrl(imageUrl)
                    widgetPrefs.setWidgetType("clock")

                    // 시계 위젯 등록
                    val providerComponent =
                        ComponentName(context, GoWatchWidgetReceiver::class.java)

                    withContext(Dispatchers.Main) {
                        val success =
                            appWidgetManager.requestPinAppWidget(providerComponent, null, null)

                        if (success) {
                            onSuccess()
                        } else {
                            onError("위젯 추가를 취소했거나 실패했습니다.")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("위젯 고정은 Android 8.0 이상에서 지원됩니다.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("위젯 등록 실패: ${e.message}")
                }
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
