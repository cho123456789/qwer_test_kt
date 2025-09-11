package com.example.qwer_test_kt.data

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import com.example.qwer_test_kt.data.repository.MemberRepositoryImpl
import com.example.qwer_test_kt.data.repository.WidgetRepositoryImpl
import com.example.qwer_test_kt.data.source.MemberRemoteDataSource
import com.example.qwer_test_kt.data.source.MemberRemoteDataSourceImpl
import com.example.qwer_test_kt.domin.repository.MemberRepository
import com.example.qwer_test_kt.domin.repository.WidgetRepository
import com.example.qwer_test_kt.gomin.wiget.BatteryGlanceWidgetProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// Dagger Hilt 의존성 주입 ( 인터페이스에 대한 구현체에 바인딩)
// -> wallpaperRepository 요청하면 WallpaperRepositoryImpl 제공해준다
@Module
@InstallIn(SingletonComponent::class)  // 바인딩이 어플리케이션 전체 적용 (생명주기)
abstract  class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRemoteDataRepository(
        memberRemoteDataSource: MemberRemoteDataSourceImpl
    ): MemberRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberRepository: MemberRepositoryImpl
    ): MemberRepository

    @Binds
    @Singleton
    abstract fun bindWidgetRepository(
        widgetRepository: WidgetRepositoryImpl
    ): WidgetRepository
}