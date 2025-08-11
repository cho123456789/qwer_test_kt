package com.example.qwer_test_kt.domin.usecase

import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 비즈니스 로직을 수행하는 유스케이스 정의
class GetMembersUseCase@Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): Result<List<Member>> {
        return repository.getMembers()
    }
}