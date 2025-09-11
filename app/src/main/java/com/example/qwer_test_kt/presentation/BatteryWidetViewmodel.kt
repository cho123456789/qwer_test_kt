package com.example.qwer_test_kt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwer_test_kt.domin.model.BatteryInfo
import com.example.qwer_test_kt.domin.model.WidgetData
import com.example.qwer_test_kt.domin.usecase.GetWidgetInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class WidgetUiState(
    val batteryInfo: BatteryInfo = BatteryInfo(0, -1),
    val widgetData: WidgetData = WidgetData(),
    val isLoading: Boolean = true
)


class BatteryWidgetViewmodel @Inject constructor(
    private val getWidgetInfoUseCase: GetWidgetInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WidgetUiState())
    val uiState: StateFlow<WidgetUiState> = _uiState.asStateFlow()

    init {
        fetchWidgetInfo()
    }

    private fun fetchWidgetInfo() {
        viewModelScope.launch {
            try {
                val widgetData = getWidgetInfoUseCase()

                // 가져온 데이터로 UI 상태 업데이트
                _uiState.value = WidgetUiState(
                    widgetData = widgetData,
                    isLoading = false
                )
            } catch (e: Exception) {
                // 에러 처리
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}