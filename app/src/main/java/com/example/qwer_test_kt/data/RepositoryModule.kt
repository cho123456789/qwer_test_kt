package com.example.qwer_test_kt.data

import com.example.qwer_test_kt.domin.repository.MemberRepository
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import com.example.qwer_test_kt.domin.usecase.GetMembersUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideMemberDataSource(firestore: FirebaseFirestore): MemberDataSource {
        return MemberDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideMemberRepository(dataSource: MemberDataSource): MemberRepository {
        return MemberRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetMembersUseCase(repository: MemberRepository): GetMembersUseCase {
        return GetMembersUseCase(repository)
    }
}