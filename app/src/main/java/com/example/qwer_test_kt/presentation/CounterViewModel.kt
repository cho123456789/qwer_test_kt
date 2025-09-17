package com.example.qwer_test_kt.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    // 버튼 클릭 시 호출될 함수
    fun increment() {
        _count.value++ // 값 증가
    }
}