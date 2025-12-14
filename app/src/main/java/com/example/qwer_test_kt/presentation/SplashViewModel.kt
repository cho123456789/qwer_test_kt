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
class SplashViewModel @Inject constructor(
    private val getAllProfilesUseCase: GetAllProfilesUseCase
) : ViewModel() {

    private val _profiles = MutableStateFlow<List<ProfileByType>?>(null)
    val profiles: StateFlow<List<ProfileByType>?> = _profiles.asStateFlow()

    init {
        viewModelScope.launch {
            val result = getAllProfilesUseCase.invoke()
            _profiles.value = result
        }
    }
}
