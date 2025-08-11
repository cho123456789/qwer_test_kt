package com.example.qwer_test_kt.data

import com.example.qwer_test_kt.domin.model.Member
import com.example.qwer_test_kt.domin.repository.MemberRepository
import javax.inject.Inject

//  DTO를 가져와 도메인 모델로 변환하는 역할

class MemberRepositoryImpl @Inject constructor(
    private val dataSource: MemberDataSource
) : MemberRepository {
    override suspend fun getMembers(): Result<List<Member>> {
        return dataSource.getMembers().map { memberDtos ->
            memberDtos.map { it.toDomain() }
        }
    }

    private fun MemberDto.toDomain(): Member {
        return Member(
            name = name,
            profileImageUrl = profileImage,
            wallPaperImageResId = wallPaperImageResId
        )
    }
}