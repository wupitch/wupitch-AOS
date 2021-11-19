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
import wupitch.android.util.TimeType
import wupitch.android.util.isEndTimeFasterThanStart
import javax.inject.Inject

@HiltViewModel
class CreateCrewViewModel @Inject constructor(
    private val getSportRepository: GetSportRepository,
    private val getDistrictRepository : GetDistrictRepository
    ) : ViewModel() {

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _crewSportId = mutableStateOf(-1)
    val crewSportId : State<Int> = _crewSportId

    private var _districtList = mutableStateOf(DistrictState())
    val districtList : State<DistrictState> = _districtList

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage : State<Boolean?> = _isUsingDefaultImage

    var imageChosenState = mutableStateOf(false)

    private var _startTime = mutableStateOf("00:00")
    val startTime : State<String> = _startTime

    private var _endTime = mutableStateOf("00:00")
    val endTime : State<String> = _endTime

    val hasStartTimeSet = mutableStateOf<Boolean?>(false)

    var hasEndTimeSet = mutableStateOf<Boolean?>(false)



    fun getSports() = viewModelScope.launch {
        _sportsList.value = SportState(isLoading = true)

        val response = getSportRepository.getSport()
        if (response.isSuccessful) {
            response.body()?.let { sportRes ->
                if (sportRes.isSuccess) {
                    _sportsList.value =
                        SportState(data = sportRes.result.filter { it.sportsId < sportRes.result.size }.map { it.toFilterItem() })
                    _sportsList.value.data.forEachIndexed { index, filterItem ->
                        if(_crewSportId.value == index){
                            filterItem.state.value = true
                        }
                    }
                }

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

    fun setCrewSport(sportId : Int) {
        _crewSportId.value = sportId
        Log.d("{CreateCrewViewModel.setCrewSport}", _crewSportId.value.toString())
    }

    fun setUserDistrict(districtId : Int, districtName : String) {
        //todo 서버에 보내기
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $districtId name : $districtName")
    }

    fun setImageSource(isUsingDefaultImage : Boolean?) {
        _isUsingDefaultImage.value = isUsingDefaultImage
    }

    fun setImageChosenState(attemptedToChoose : Boolean) {
        imageChosenState.value = attemptedToChoose
    }

    fun setTimeFilter(type : TimeType, hour : Int, min : Int) {
        var hourString = hour.toString()
        var minString = min.toString()
        if(hour < 10) hourString = "0$hour"
        if(min < 10) minString = "0$min"
        val timeString = "$hourString:$minString"

        when(type){
            TimeType.START -> {
                if(hasEndTimeSet.value == true){
                    if(isEndTimeFasterThanStart(timeString, _endTime.value)) {
                        _startTime.value = "00:00"
                        hasStartTimeSet.value = null
                    }else {
                        _startTime.value = timeString
                        hasStartTimeSet.value = true
                    }
                }else {
                    _startTime.value = timeString
                    hasStartTimeSet.value = true
                }
            }
            TimeType.END -> {

                if(isEndTimeFasterThanStart(_startTime.value, timeString)) {
                    _endTime.value = "00:00"
                    hasEndTimeSet.value = null
                }
                else {
                    _endTime.value = timeString
                    hasEndTimeSet.value = true
                }
            }
        }
    }
}