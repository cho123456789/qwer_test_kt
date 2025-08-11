package com.example.qwer_test_kt.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


// 파일에 Firestore와 직접 통신하는 로직을 구현
/**
 * Firebase Firestore 데이터에 접근하는 역할을 담당합니다.
 * 데이터를 가져오거나 업데이트하는 모든 로직이 이 클래스에 집중됩니다.
 */

class MemberDataSource(
    private val firestore: FirebaseFirestore
) {
    suspend fun getMembers(): Result<List<MemberDto>> {
        return try {
            val snapshot = firestore.collection("qwer/gomin/members").get().await()
            val memberDtos = snapshot.documents.mapNotNull { it.toObject(MemberDto::class.java) }
            Result.success(memberDtos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}