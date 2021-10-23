package wupitch.android.presentation.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SignupViewModel : ViewModel() {

    private var _userRegion = MutableLiveData<String>()
    val userRegion : LiveData<String> = _userRegion


    fun setUserRegion(region : String) {
        //서버에 보내기 또는 데이터에 넣어두기?
        _userRegion.value = region
        Log.d("{SignupViewModel.getUserRegion}", userRegion.value.toString())
    }
}