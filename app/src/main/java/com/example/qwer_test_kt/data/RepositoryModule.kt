package com.example.qwer_test_kt.data

import com.example.qwer_test_kt.data.repository.BatteryRepositoryImpl
import com.example.qwer_test_kt.data.repository.MemberRepositoryImpl
import com.example.qwer_test_kt.data.repository.WidgetRepositoryImpl
import com.example.qwer_test_kt.domin.repository.BatteryRepository
import com.example.qwer_test_kt.domin.repository.MemberRepository
import com.example.qwer_test_kt.domin.repository.WallpaperRepository
import com.example.qwer_test_kt.domin.repository.WidgetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// Dagger Hilt 의존성 주입 ( 인터페이스에 대한 구현체에 바인딩)
// -> wallpaperRepository 요청하면 WallpaperRepositoryImpl 제공해준다
@Module
@InstallIn(SingletonComponent::class)  // 바인딩이 어플리케이션 전체 적용 (생명주기)
abstract  class RepositoryModule {
    @Binds  // 인터페이스를 구현체에 연결 ( 함수 파라미터 하나만 존재) / 반환체가 인터페이스
    @Singleton // 인스턴스가 앱 전체에서 하나의 인스턴스로만 존재
    abstract fun bindWallpaperRepository(
        wallpaperRepository: WallpaperRepositoryImpl
    ): WallpaperRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(
        memberRepository: MemberRepositoryImpl
    ): MemberRepository

    @Binds
    @Singleton
    abstract fun bindBatteryRepository(
        batteryRepository: BatteryRepositoryImpl
    ): BatteryRepository

    @Binds
    @Singleton
    abstract fun bindWidgetRepository(
        widgetRepository: WidgetRepositoryImpl
    ): WidgetRepository
}