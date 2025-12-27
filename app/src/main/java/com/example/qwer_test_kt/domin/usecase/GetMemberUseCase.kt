package com.example.qwer_test_kt.domin.usecase

import com.example.qwer_test_kt.data.model.MemberMainData
import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.model.MemberDetail
import com.example.qwer_test_kt.domin.model.ProfileByType
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

// 메인 이미지 가져오기
class GetMainImageUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): List<MemberMainData> {
        return repository.getMainImage()
    }
}

// 타입별 메인 이미지 가져오기 (각 테이블에서)
class GetMainImagesByTypeUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(typeName: String): List<MemberMainData> {
        return repository.getMainImagesByType(typeName)
    }
}

// 특정 타입의 프로필 데이터 가져오기 (디스코드, 고민중독, 내이름맑음, 눈물참기)
class GetProfilesByTypeUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(typeName: String): ProfileByType? {
        return repository.getProfilesByType(typeName)
    }
}

// 모든 프로필 타입 데이터 가져오기
class GetAllProfilesUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): List<ProfileByType> {
        return repository.getAllProfiles()
    }
}

class GetMemberDetailsUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): List<MemberDetail> {
        return memberRepository.getMemberDetails()
    }
}