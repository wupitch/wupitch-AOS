package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import wupitch.android.data.repository.GetDistrictRepositoryImpl
import wupitch.android.data.repository.KakaoLoginRepositoryImpl
import wupitch.android.domain.repository.GetDistrictRepository
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
    @Singleton //왜 싱클턴??
    fun provideGetDistrictRepository(retrofit: Retrofit) : GetDistrictRepository {
        return GetDistrictRepositoryImpl(retrofit)
    }
}