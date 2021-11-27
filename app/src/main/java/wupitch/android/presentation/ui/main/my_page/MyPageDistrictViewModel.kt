package wupitch.android.presentation.ui.main.my_page

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.ProfileRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import javax.inject.Inject

@HiltViewModel
class MyPageDistrictViewModel @Inject constructor(
    private val getDistrictRepository: GetDistrictRepository,
    private val profileRepository: ProfileRepository,
    ) : ViewModel() {

    private var _districtState = mutableStateOf(BaseState())
    val districtState : State<BaseState> = _districtState

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)
    val userDistrictId: State<Int?> = _userDistrictId

    private var _userDistrictName = mutableStateOf<String?>(null)
    val userDistrictName: State<String?> = _userDistrictName


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

    fun getUserDistrict () = viewModelScope.launch {
        val response = profileRepository.getUserDistrict()

        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _userDistrictId.value = res.result.areaId
                    _userDistrictName.value = res.result.areaName
                }
            }
        }
    }

    fun changeUserDistrict() = viewModelScope.launch {
        _districtState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            areaId = _userDistrictId.value!! + 1
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _districtState.value = BaseState(isSuccess = true)
                else _districtState.value = BaseState(error = it.message)
            }
        }else _districtState.value = BaseState(error = "지역 변경에 실패했습니다.")
    }
}