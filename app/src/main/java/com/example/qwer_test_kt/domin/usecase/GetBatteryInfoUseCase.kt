package com.example.qwer_test_kt.domin.usecase

import com.example.qwer_test_kt.domin.model.BatteryInfo
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.repository.BatteryRepository
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 비즈니스 로직 (구체적인 실행 계획)
class GetBatteryInfoUseCase @Inject constructor(
    private val batteryRepository: BatteryRepository
) {
    suspend operator fun invoke(): BatteryInfo{
        return batteryRepository.getBatteryInfo()
    }
}