package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import wupitch.android.data.repository.*
import wupitch.android.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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

    @Provides
    @Singleton
    fun provideSignupRepository(retrofit: Retrofit) : SignupRepository {
        return SignupRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(retrofit: Retrofit) : LoginRepository {
        return LoginRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideCrewRepository(retrofit: Retrofit) : CrewRepository {
        return CrewRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideImprtRepository(retrofit: Retrofit) : ImprtRepository {
        return ImprtRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(retrofit: Retrofit) : ProfileRepository {
        return ProfileRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideFcmRepository(retrofit: Retrofit) : FcmRepository {
        return FcmRepositoryImpl(retrofit)
    }
}