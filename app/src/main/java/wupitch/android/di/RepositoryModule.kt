package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import wupitch.android.data.repository.CheckValidRepositoryImpl
import wupitch.android.data.repository.GetDistrictRepositoryImpl
import wupitch.android.data.repository.GetSportRepositoryImpl
import wupitch.android.data.repository.KakaoLoginRepositoryImpl
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.domain.repository.KakaoLoginRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton //왜 싱클턴??
    fun provideKakaoLoginRepository(retrofit: Retrofit) : KakaoLoginRepository {
        return KakaoLoginRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideGetDistrictRepository(retrofit: Retrofit) : GetDistrictRepository {
        return GetDistrictRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideGetSportRepository(retrofit: Retrofit) : GetSportRepository {
        return GetSportRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideCheckValidRepository(retrofit: Retrofit) : CheckValidRepository {
        return CheckValidRepositoryImpl(retrofit)
    }
}