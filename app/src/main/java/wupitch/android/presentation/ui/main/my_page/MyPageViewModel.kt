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
import kotlinx.coroutines.launch
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.home.create_crew.SportState
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val getDistrictRepository: GetDistrictRepository,
    private val getSportRepository: GetSportRepository,
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

}