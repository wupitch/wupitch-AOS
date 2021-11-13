package wupitch.android.presentation.ui.main.home.create_crew

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import javax.inject.Inject

@HiltViewModel
class CreateCrewViewModel @Inject constructor(
    private val getSportRepository: GetSportRepository,
    private val getDistrictRepository : GetDistrictRepository
    ) : ViewModel() {

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _districtList = mutableStateOf(DistrictState())
    val districtList : State<DistrictState> = _districtList

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    private var _userDongId = MutableLiveData<Int>()
    val userDongId : LiveData<Int> = _userDongId

    private var _userDongName = MutableLiveData<String>()
    val userDongName : LiveData<String> = _userDongName

    private var _isUsingDefaultImage = mutableStateOf<Boolean>(false)
    val isUsingDefaultImage : State<Boolean> = _isUsingDefaultImage

    var imageChosenState = mutableStateOf(false)


    fun getSports() = viewModelScope.launch {
        _sportsList.value = SportState(isLoading = true)

        val response = getSportRepository.getSport()
        if (response.isSuccessful) {
            response.body()?.let { sportRes ->
                if (sportRes.isSuccess) _sportsList.value =
                    SportState(data = sportRes.result.map { it.toFilterItem() })
                else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }

    fun getDistricts() = viewModelScope.launch {
        _districtList.value = DistrictState(isLoading = true)

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let { districtRes ->
                if(districtRes.isSuccess) _districtList.value = DistrictState(data = districtRes.result.map { it.name }.toTypedArray())
                else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")
            }
        } else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")

    }

    fun setUserDistrict(districtId : Int, districtName : String) {
        //todo 서버에 보내기
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $districtId name : $districtName")
    }

    fun setUserDong(dongId : Int, dongName : String) {
        //todo 서버에 보내기
        _userDongId.value = dongId
        _userDongName.value = dongName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $dongId name : $dongName")
    }

    fun setImageSource(isUsingDefaultImage : Boolean) {
        _isUsingDefaultImage.value = isUsingDefaultImage
    }

    fun setImageChosenState(attemptedToChoose : Boolean) {
        imageChosenState.value = attemptedToChoose
    }
}