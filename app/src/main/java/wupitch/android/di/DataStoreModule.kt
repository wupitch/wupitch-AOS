package wupitch.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wupitch.android.CrewFilter
import com.wupitch.android.ImpromptuFilter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import wupitch.android.common.Constants.crewFilterStore
import wupitch.android.common.Constants.impromptuFilterStore
import wupitch.android.common.Constants.userInfoStore
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object DataStoreModule {

    @Provides
    @ViewModelScoped
    fun provideCrewFilterDataStore(
        @ApplicationContext context: Context
    ) : DataStore<CrewFilter> {
        return context.crewFilterStore
    }

    @Provides
    @ViewModelScoped
    fun provideImpromptuFilterDataStore(
        @ApplicationContext context: Context
    ) : DataStore<ImpromptuFilter> {
        return context.impromptuFilterStore
    }

    @Provides
    @ViewModelScoped
    fun provideUserInfoDataStore(
        @ApplicationContext context: Context
    ) : DataStore<Preferences> {
        return context.userInfoStore
    }
}