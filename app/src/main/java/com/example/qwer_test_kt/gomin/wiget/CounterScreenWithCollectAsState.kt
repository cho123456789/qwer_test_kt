package com.example.qwer_test_kt.gomin.wiget

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qwer_test_kt.presentation.CounterViewModel

@Composable
fun CounterScreenWithCollectAsState(
    viewModel: CounterViewModel = viewModel()
) {
    // collectAsState()를 사용하여 Flow를 State로 변환
    // 이 Composable이 활성 상태일 때 Flow를 수집
    val countState = viewModel.count.collectAsState()

    Column {
        // State 객체의 .value를 읽어서 UI에 반영
        Text(text = "Count: ${countState.value}")

        Button(onClick = { viewModel.increment() }) {
            Text(text = "Increment")
        }
    }
}