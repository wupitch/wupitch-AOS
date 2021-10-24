package wupitch.android.presentation.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SignupViewModel : ViewModel() {


    private var _userRegion = MutableLiveData<String>()
    val userRegion : LiveData<String> = _userRegion

    private var _isNicknameValid = MutableLiveData<Boolean>()
    val isNicknameValid : LiveData<Boolean> = _isNicknameValid

    private var _userNickname = MutableLiveData<String>()

    private var _userIntroduction = MutableLiveData<String>()
//    val userIntroduction : LiveData<String> = _userIntroduction

    private var _isProfileBtnActivated = MutableLiveData<Boolean>()
    val isProfileBtnActivated : LiveData<Boolean> = _isProfileBtnActivated



    fun setUserRegion(region : String) {
        //서버에 보내기 또는 데이터에 넣어두기?
        _userRegion.value = region
        Log.d("{SignupViewModel.getUserRegion}", userRegion.value.toString())
    }

    fun checkNicknameValidation(nickname : String) {
        //서버 validation
        //okay 이면,
        Log.d("{SignupViewModel.checkNicknameValidation}", nickname)
        _isNicknameValid.value = true
        _userNickname.value = nickname
        if(_userIntroduction.value != null) _isProfileBtnActivated.value = true
    }

    fun setUserIntroduction(intro: String) {
        _userIntroduction.value = intro
        Log.d("{SignupViewModel.setUserIntroduction}", _userIntroduction.value.toString())
        if(_userNickname.value != null) _isProfileBtnActivated.value = true
    }

    fun setUserEtcSport(etcSport : String) {
        Log.d("{SignupViewModel.setUserEtcSport}", etcSport)
    }

    fun getUserSport(sport : ArrayList<String>) {
        //todo : send sport to server!
        //list 로 받아서 서버에 전해주어야 한다면...
    }
}