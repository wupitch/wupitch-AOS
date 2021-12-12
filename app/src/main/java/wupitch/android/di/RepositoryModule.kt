package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import wupitch.android.data.repository.*
import wupitch.android.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideGetDistrictRepository(retrofit: Retrofit) : GetDistrictRepository {
        return GetDistrictRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideGetSportRepository(retrofit: Retrofit) : GetSportRepository {
        return GetSportRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideCheckValidRepository(retrofit: Retrofit) : CheckValidRepository {
        return CheckValidRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideSignupRepository(retrofit: Retrofit) : SignupRepository {
        return SignupRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideLoginRepository(retrofit: Retrofit) : LoginRepository {
        return LoginRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideCrewRepository(retrofit: Retrofit) : CrewRepository {
        return CrewRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideImprtRepository(retrofit: Retrofit) : ImprtRepository {
        return ImprtRepositoryImpl(retrofit)
    }

    @Provides
    @ViewModelScoped
    fun provideProfileRepository(retrofit: Retrofit) : ProfileRepository {
        return ProfileRepositoryImpl(retrofit)
    }

}