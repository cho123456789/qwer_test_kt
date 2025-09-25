package com.example.qwer_test_kt.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.geometry.Offset
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.usecase.GetMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


// 비즈니스 로직 실행 -> UI 연결

@HiltViewModel
class GominJungdokViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {

    val _selectedMemberName = MutableStateFlow("전체")

    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members

    fun setWallpaper(context: Context, wallpaperUrl: String, onIntentReady: (Intent) -> Unit) {
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
            }
        }
    }

    val filterWallpapers: StateFlow<List<String>> =
        combine(members, _selectedMemberName) { memberList, selectedName ->
            if (selectedName == "전체") {
                memberList.flatMap { it.wallpaperImageUrls }
            } else {
                memberList.find { it.name == selectedName }?.wallpaperImageUrls ?: emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadMembers()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            _members.value = getMemberUseCase()
        }
    }

    fun filterByMember(memberName: String) {
        _selectedMemberName.value = memberName
    }
}

fun saveBitmapToTempFile(context: Context, bitmap: Bitmap): File {
    val tempFile = File(context.cacheDir, "wallpaper_temp.png")
    val fos = FileOutputStream(tempFile)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    fos.flush()
    fos.close()
    return tempFile
}

suspend fun downloadBitmap(context: Context, url: String): Bitmap? {
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false)
        .build()
    val result = context.imageLoader.execute(request)
    return (result.drawable as? BitmapDrawable)?.bitmap
}