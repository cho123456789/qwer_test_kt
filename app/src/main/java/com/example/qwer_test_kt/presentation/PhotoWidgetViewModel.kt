package com.example.qwer_test_kt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwer_test_kt.domin.model.ProfileByType
import com.example.qwer_test_kt.domin.usecase.GetAllProfilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
            }
        }
    }
}
