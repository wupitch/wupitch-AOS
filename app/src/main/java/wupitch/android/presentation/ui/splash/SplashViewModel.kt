package wupitch.android.presentation.ui.splash

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {


    suspend fun readJwt() : String? {

        //development 용도
//        context.dataStore.edit { settings ->
//            settings[Constants.JWT_PREFERENCE_KEY] = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aXZpZHN3YW4xMkBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjM4NTYwODg4LCJleHAiOjE2NDcyMDA4ODh9.MMR3wCQWCz97aouBuf8OwLKpeIHT31jchEbyu4PaFb4"
//            settings[Constants.USER_ID] = 100
//            settings[Constants.USER_NICKNAME] = "베키쨩"
//        }

        val jwtPreferenceFlow = context.dataStore.data.first()
        return jwtPreferenceFlow[Constants.JWT_PREFERENCE_KEY]
    }
}