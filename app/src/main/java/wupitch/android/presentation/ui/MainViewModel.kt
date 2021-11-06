package wupitch.android.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.domain.model.DistrictResult
import wupitch.android.domain.repository.GetDistrictRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository
) : ViewModel() {

    private var _district = MutableLiveData<Resource<DistrictRes>>()
    val district : LiveData<Resource<DistrictRes>> = _district

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    private var _isNicknameValid = MutableLiveData<Boolean>()
    val isNicknameValid : LiveData<Boolean> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()

    private var _userIntroduction = MutableLiveData<String?>()
//    val userIntroduction : LiveData<String> = _userIntroduction


    private var _userSportTalent = MutableLiveData<Int?>()
    val userSportTalent : LiveData<Int?> = _userSportTalent

    private var _userSport = MutableLiveData<Int>()
    val userSport : LiveData<Int> = _userSport

    private var _userAge = MutableLiveData<Int>()
    val userAge : LiveData<Int> = _userAge



    fun getDistricts () = viewModelScope.launch {
        _district.value = Resource.Loading<DistrictRes>()

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let {
                if(it.isSuccess) _district.value = Resource.Success<DistrictRes>(it)
                else _district.value = Resource.Error<DistrictRes>(null, "지역 가져오기를 실패했습니다.")
            }
        } else _district.value = Resource.Error<DistrictRes>(null, "지역 가져오기를 실패했습니다.")

    }

    fun setUserRegion(districtId : Int, districtName : String) {
        //todo 서버에 보내기
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{SignupViewModel.getUserRegion}", "id : $districtId name : $districtName")
    }

    fun checkNicknameValidation(nickname : String?) {
        //서버 validation
        //okay 이면,
        Log.d("{SignupViewModel.checkNicknameValidation}", nickname.toString())
        _isNicknameValid.value = true
        _userNickname.value = nickname
    }

    fun setUserIntroduction(intro: String?) {
        _userIntroduction.value = intro
        Log.d("{SignupViewModel.setUserIntroduction}", _userIntroduction.value.toString())
    }

    fun setUserEtcSport(etcSport : String) {
        Log.d("{SignupViewModel.setUserEtcSport}", etcSport)
    }

    fun getUserSport(sport : ArrayList<String>) {
        //todo : send sport to server!
        //list 로 받아서 서버에 전해주어야 한다면...
    }

    fun getUserSportTalent(talentCode : Int?, sportCode: Int) {
        //todo : talentCode 가 null 이 아니라면, 종목 별로 실력이랑 짝지어서 서버에 보내기!!! int or string?
        Log.d("{SignupViewModel.getUserSportTalent}", "talentCode : $talentCode sportCode : $sportCode")
        _userSportTalent.value = talentCode
        _userSport.value = sportCode

    }


    fun setUserAge(ageCode : Int) {
        _userAge.value = ageCode
        Log.d("{SignupViewModel.setUserAge}", _userAge.value.toString())
    }
}