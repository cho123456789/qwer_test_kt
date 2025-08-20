package com.example.qwer_test_kt.domin.usecase

import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 비즈니스 로직 (구체적인 실행 계획)
class GetMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): List<Member> {
        return memberRepository.getMember()
    }
}