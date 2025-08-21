package com.example.qwer_test_kt.domin.usecase

import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.WidgetData
import com.example.qwer_test_kt.domin.repository.MemberRepository
import com.example.qwer_test_kt.domin.repository.WidgetRepository
import javax.inject.Inject

// 비즈니스 로직 (구체적인 실행 계획)
class GetWidgetInfoUseCase @Inject constructor(
    private val widgetRepository: WidgetRepository
) {
    suspend operator fun invoke(): WidgetData {
        return widgetRepository.getWidgetData()
    }
}