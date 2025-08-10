package com.example.qwer_test_kt.presentation

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.ui.geometry.Offset
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwer_test_kt.domin.usecase.GetWallpaperBitmapUseCase
import com.example.qwer_test_kt.domin.usecase.SetWallpaperUseCase
import com.example.qwer_test_kt.gomin.view.downloadBitmap
import com.example.qwer_test_kt.gomin.view.saveBitmapToTempFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// 사용자가 배경화면 설정 ->  viewmodeldl setWallpaterUseCase 호출
// 도메인 영역 -> setWallpaterUseCase -> repostiory에 있는 setWallpaper 호출 배경화면 설정 요청
// 데이터 영역 -> usecase -> WallpaperRepositoryImpl 구현체와 연결 => 실제 로직 적용

// usecase를 행위를 지정하고 리포지토리 인터페이스를 통해서 행위를 데이터 계층으로 보낸다
data class GominJungdokUiState(
    val selectedWallpaper: String? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val imageScale: Float = 1f,
    val imageOffset: Offset = Offset.Zero
)

@HiltViewModel
class GominJungdokViewModel @Inject constructor(
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val getWallpaperBitmapUseCase: GetWallpaperBitmapUseCase,
    private val getApplication: Application
) : ViewModel() {

    // 내부에서 상태를 변경
    private val _uiState = MutableStateFlow(GominJungdokUiState())

    // viewmodel 변경된 상태 확인
    val uiState: StateFlow<GominJungdokUiState> = _uiState.asStateFlow()

    fun setWallpaper(context: Context, wallpaperUrl: String, onIntentReady: (Intent) -> Unit) {
        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO) {
                val bitmap = downloadBitmap(context, wallpaperUrl)
                bitmap?.let {
                    val wallpaperFile = saveBitmapToTempFile(context, it)
                    val contentUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        wallpaperFile
                    )
                    val intent = Intent(Intent.ACTION_SET_WALLPAPER).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        setDataAndType(contentUri, "image/*")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    withContext(Dispatchers.Main) {
                        onIntentReady(intent)
                    }
                } ?: run {
                    _uiState.update {
                        it.copy(userMessage = "이미지를 다운로드할 수 없습니다.")
                    }
                }
            }
        }
    }

    fun updateImageTransform(scale: Float, offset: Offset) {
        _uiState.update {
            it.copy(imageScale = scale, imageOffset = offset)
        }
    }

    fun onWallpaperSelected(url: String) {
        _uiState.update {
            it.copy(selectedWallpaper = url)
        }
    }

    fun onBackPressed() {
        _uiState.update {
            it.copy(selectedWallpaper = null)
        }
    }

    fun userMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }
}
