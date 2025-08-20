package com.example.qwer_test_kt.data

import com.example.qwer_test_kt.data.repository.MemberRepositoryImpl
import com.example.qwer_test_kt.domin.repository.MemberRepository
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        wallpaperRepository: WallpaperRepositoryImpl
    ):WallpaperRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberRepository: MemberRepositoryImpl
    ):MemberRepository
}