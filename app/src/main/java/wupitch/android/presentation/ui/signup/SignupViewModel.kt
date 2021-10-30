package wupitch.android.presentation.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SignupViewModel : ViewModel() {


    private var _userRegion = MutableLiveData<String>()
    val userRegion : LiveData<String> = _userRegion

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



    fun setUserRegion(region : String) {
        //서버에 보내기 또는 데이터에 넣어두기?
        _userRegion.value = region
        Log.d("{SignupViewModel.getUserRegion}", userRegion.value.toString())
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