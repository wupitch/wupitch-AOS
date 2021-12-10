package wupitch.android.presentation.ui.splash

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import wupitch.android.common.Constants
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userInfoDataStore : DataStore<Preferences>
) : ViewModel() {


    suspend fun readJwt() : String? {

        //development 용도
//        context.dataStore.edit { settings ->
//            settings[Constants.JWT_PREFERENCE_KEY] = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aXZpZHN3YW4xMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM4NTYwODg4LCJleHAiOjE2NDcyMDA4ODh9.MMR3wCQWCz97aouBuf8OwLKpeIHT31jchEbyu4PaFb4"
//            settings[Constants.USER_ID] = 100
//            settings[Constants.USER_NICKNAME] = "베키쨩"
//        }

        val prefFlow = userInfoDataStore.data.first()
        return prefFlow[Constants.JWT_PREFERENCE_KEY]
    }
}