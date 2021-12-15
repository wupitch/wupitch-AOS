package wupitch.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import wupitch.android.util.GetImageFile

@Module
@InstallIn(ViewModelComponent::class)
object ImageModule {

    @Provides
    @ViewModelScoped
    fun provideGetRealPath(
        @ApplicationContext context: Context
    ) : GetImageFile {
        return GetImageFile(context)
    }
}