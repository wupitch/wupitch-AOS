package wupitch.android.presentation.ui.splash

import android.content.Context
import android.util.Log
import androidx.compose.material.ContentAlpha
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import wupitch.android.WupitchApplication
import wupitch.android.WupitchApplication.Companion.dataStore
import wupitch.android.common.Constants
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