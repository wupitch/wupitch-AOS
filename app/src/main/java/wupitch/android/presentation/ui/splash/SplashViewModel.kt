package wupitch.android.presentation.ui.splash

import android.content.Context
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
        val jwtPreferenceFlow = context.dataStore.data.first()
        return jwtPreferenceFlow[Constants.JWT_PREFERENCE_KEY]
    }
}