package wupitch.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import wupitch.android.WupitchApplication
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardModule {

//    @Provides
//    fun provideApplication() {
//        return WupitchApplication
//    }
}