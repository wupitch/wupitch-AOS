package wupitch.android.presentation.ui.main.my_page

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.ChangePwReq
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.domain.repository.ProfileRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.home.create_crew.SportState
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val getDistrictRepository: GetDistrictRepository,
    private val getSportRepository: GetSportRepository,
    private val profileRepository: ProfileRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    //profile
    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    val userNickname: LiveData<String?> = _userNickname

    private var _userIntroduce = MutableLiveData<String>()
    val userIntroduce: LiveData<String> = _userIntroduce

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId: LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName: LiveData<String> = _userDistrictName

    //sports
    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _userSportId = mutableStateOf(-1)
    val userSportId: State<Int> = _userSportId

    //age group
    private var _userAge = MutableLiveData<Int>()
    val userAge: LiveData<Int> = _userAge

    //contact
    var userPhoneNum = mutableStateOf("")

    //notification
    var notificationState = mutableStateOf(false)

    //password
    private var _isCurrentPwValid = mutableStateOf<Boolean?>(null)
    val isCurrentPwValid : State<Boolean?> = _isCurrentPwValid

    private var _isNewPwValid = mutableStateOf<Boolean?>(null)
    val isNewPwValid : State<Boolean?> = _isNewPwValid

    private var _userNewPw = mutableStateOf("")

    private var _changePwState = mutableStateOf(BaseState())
    val changePwState : State<BaseState> = _changePwState


    fun checkNicknameValid(nickname: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkNicknameValid}", nickname)
        if (nickname.isEmpty()) {
            _isNicknameValid.value = null
            return@launch
        }

        val response = checkValidRepository.checkNicknameValidation(NicknameValidReq(nickname))
        if (response.isSuccessful) {
            response.body()?.let { validRes ->
                if (validRes.isSuccess) {
                    _isNicknameValid.value = true
                    _userNickname.value = nickname
                } else {
                    _isNicknameValid.value = false
                }
            }
        } else _isNicknameValid.value = false

    }

    fun setUserIntroduce(introduce: String) {
        _userIntroduce.value = introduce
    }

    fun getDistricts() = viewModelScope.launch {
        _districtList.value = DistrictState(isLoading = true)

        val response = getDistrictRepository.getDistricts()
        if (response.isSuccessful) {
            response.body()?.let { districtRes ->
                if (districtRes.isSuccess) _districtList.value =
                    DistrictState(data = districtRes.result.map { it.name }.toTypedArray())
                else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")
            }
        } else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")

    }

    fun setUserDistrict(districtId: Int, districtName: String) {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{SignupViewModel.getUserRegion}", "id : $districtId name : $districtName")
    }

    fun getSports() = viewModelScope.launch {
        _sportsList.value = SportState(isLoading = true)

        val response = getSportRepository.getSport()
        if (response.isSuccessful) {
            response.body()?.let { sportRes ->
                if (sportRes.isSuccess) {
                    _sportsList.value =
                        SportState(data = sportRes.result.filter { it.sportsId < sportRes.result.size }
                            .map { it.toFilterItem() })
                    _sportsList.value.data.forEachIndexed { index, filterItem ->
                        if (_userSportId.value == index) {
                            filterItem.state.value = true
                        }
                    }
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }

    fun setUserSport(sportId: Int) {
        _userSportId.value = sportId
        Log.d("{CreateCrewViewModel.setCrewSport}", _userSportId.value.toString())
    }

    fun setUserAge(ageCode: Int) {
        _userAge.value = ageCode
        Log.d("{SignupViewModel.setUserAge}", _userAge.value.toString())
    }

    fun setUserPhoneNum(phoneNum: String) {
        userPhoneNum.value = phoneNum
    }

    fun logoutUnregisterUser() = viewModelScope.launch {

        context.dataStore.edit { settings ->
            settings[Constants.JWT_PREFERENCE_KEY] = ""
            settings[Constants.USER_ID] = -1
            settings[Constants.USER_NICKNAME] = ""
        }
    }

    fun checkCurrentPwValid(pw : String) = viewModelScope.launch {
        if (pw.isEmpty()) {
            _isCurrentPwValid.value = null
            return@launch
        }

        //todo
        delay(1000L)
        _isCurrentPwValid.value = true

//        val response = checkValidRepository.checkEmailValidation(EmailValidReq(email))
//        if (response.isSuccessful) {
//            response.body()?.let { validRes ->
//                if (validRes.isSuccess) {
//                    _isEmailValid.value = true
//                    _userEmail.value = email
//                } else {
//                    _isEmailValid.value = false
//                }
//            }
//        } else _isEmailValid.value = false
    }

    fun checkNewPwValid(pw : String) {
        if (pw.isEmpty()) {
            _isNewPwValid.value = null
            return
        }
        val isPwValid = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$".toRegex())
        if (isPwValid) {
            _isNewPwValid.value = true
            _userNewPw.value = pw
        } else {
            _isNewPwValid.value = false
        }
    }

    fun changePw() = viewModelScope.launch {
        _changePwState.value = BaseState(isLoading = true)
        val response = profileRepository.changePw(ChangePwReq(password = _userNewPw.value))
        if(response.isSuccessful){
            response.body()?.let { baseRes ->
                if(baseRes.isSuccess)  _changePwState.value = BaseState(isSuccess = true)
                else  _changePwState.value = BaseState(error = baseRes.message)
            }
        }else  _changePwState.value = BaseState(error = "비밀번호 변경을 실패했습니다.")
    }

}