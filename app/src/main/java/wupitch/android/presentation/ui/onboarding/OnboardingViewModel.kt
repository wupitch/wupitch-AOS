package wupitch.android.presentation.ui.onboarding

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import wupitch.android.common.Constants
import wupitch.android.common.Constants.userInfoStore
import wupitch.android.domain.model.LoginReq
import wupitch.android.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userInfoDataStore : DataStore<Preferences>,
    private val loginRepository: LoginRepository,
) : ViewModel() {

    var userEmail = mutableStateOf("")
    var userPw = mutableStateOf("")

    private var _loginState = mutableStateOf(LoginState())
    val loginState : State<LoginState> = _loginState

    fun tryLogin() = viewModelScope.launch {
        _loginState.value = LoginState(isLoading = true)

        val loginReq = LoginReq(
            email = userEmail.value,
            password = userPw.value
        )
        val response = loginRepository.login(loginReq)

        if(response.isSuccessful) {
            response.body()?.let { loginRes ->
                if(loginRes.isSuccess) {
                    _loginState.value = LoginState(isSuccess = true)
                    userInfoDataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = loginRes.result.jwt
                        settings[Constants.USER_ID] = loginRes.result.accountId
                        settings[Constants.USER_EMAIL] = loginRes.result.email
                    }
                }
                else _loginState.value = LoginState(error = loginRes.message)
            }
        }else{
            _loginState.value = LoginState(error = "로그인에 실패했습니다.")
        }
    }

}