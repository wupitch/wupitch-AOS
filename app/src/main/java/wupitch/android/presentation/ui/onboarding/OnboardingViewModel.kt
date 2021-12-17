package wupitch.android.presentation.ui.onboarding

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wupitch.android.CrewFilter
import com.wupitch.android.ImpromptuFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Constants.FIRST_COMER
import wupitch.android.common.Constants.JWT_PREFERENCE_KEY
import wupitch.android.common.Constants.USER_ID
import wupitch.android.domain.model.LoginReq
import wupitch.android.domain.repository.LoginRepository
import wupitch.android.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userInfoDataStore : DataStore<Preferences>,
    private val crewFilterDataStore: DataStore<CrewFilter>,
    private val imprtFilterDataStore: DataStore<ImpromptuFilter>,
    private val loginRepository: LoginRepository,
    private val profileRepository: ProfileRepository
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
                    userInfoDataStore.edit { settings ->
                        settings[JWT_PREFERENCE_KEY] = loginRes.result.jwt
                    }
                    checkIsUserConfirmed()
                }
                else _loginState.value = LoginState(error = loginRes.message)
            }
        }else{
            _loginState.value = LoginState(error = "로그인에 실패했습니다.")
        }
    }

    private fun checkIsUserConfirmed() = viewModelScope.launch{
        val response = profileRepository.getUserInfo()
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) {
                   if(res.result.isChecked){
                       userInfoDataStore.edit { settings ->
                           settings[USER_ID] = res.result.accountId
                       }
                       crewFilterDataStore.updateData {
                           it.toBuilder()
                               .setSize(-1)
                               .build()
                       }
                       imprtFilterDataStore.updateData {
                           it.toBuilder()
                               .setSchedule(-1)
                               .setRecruitSize(-1)
                               .build()
                       }
                       _loginState.value = LoginState(isSuccess = true)
                   }else {
                       _loginState.value = LoginState(error = "신분증 인증 검토중입니다.")
                   }
                }else {
                    _loginState.value = LoginState(error = res.message)
                }
            }
        }else  _loginState.value = LoginState(error = "로그인에 실패했습니다.")
    }

}