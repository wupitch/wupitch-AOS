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
        context.dataStore.edit { settings ->
            settings[Constants.JWT_PREFERENCE_KEY] = ""
            settings[Constants.USER_ID] = -1
            settings[Constants.USER_NICKNAME] = ""
        }

        val jwtPreferenceFlow = context.dataStore.data.first()
        return jwtPreferenceFlow[Constants.JWT_PREFERENCE_KEY]
    }
}