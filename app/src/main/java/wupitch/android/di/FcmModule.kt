package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import wupitch.android.data.repository.FcmRepositoryImpl
import wupitch.android.domain.repository.FcmRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FcmModule {

    @Provides
    @Singleton
    fun provideFcmRepository(retrofit: Retrofit) : FcmRepository {
        return FcmRepositoryImpl(retrofit)
    }
}