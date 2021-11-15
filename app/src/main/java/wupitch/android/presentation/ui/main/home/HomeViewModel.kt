package wupitch.android.presentation.ui.main.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.util.TimeType
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository
) : ViewModel() {

    val loading = mutableStateOf(false)
    val crewList : MutableState<List<CrewCardInfo>> = mutableStateOf(
        listOf<CrewCardInfo>(
            CrewCardInfo(
                0,
                "축구",
                "법정동",
                true,
                "가나다라마바사아자차...",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37"
            ),
            CrewCardInfo(
                1,
                "농구",
                "이매동",
                false,
                "농구하자고고씽",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                23,
                "농구",
                "이매동",
                false,
                "아무이름이지롱",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                44,
                "농구",
                "이매동",
                false,
                "농구하자히히",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                54,
                "농구",
                "이매동",
                false,
                "농구하자고고씽하하",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                56,
                "농구",
                "이매동",
                false,
                "농구하자고고씽호호",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                77,
                "농구",
                "이매동",
                false,
                "농구하자고고씽ㅇㅇㅇ",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            )
        )
    )

    private var _district = MutableLiveData<Resource<Array<String>>>()
    val district : LiveData<Resource<Array<String>>> = _district

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    private var _startTime = mutableStateOf("00:00")
    val startTime : State<String> = _startTime

    private var _endTime = mutableStateOf("00:00")
    val endTime : State<String> = _endTime

    val hasStartTimeSet = mutableStateOf<Boolean?>(false)

    var hasEndTimeSet = mutableStateOf<Boolean?>(false)



    fun setUserRegion(districtId : Int, districtName : String) {
        //todo 서버에 보내기
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{HomeViewModel.setUserRegion}", "id : $districtId name : $districtName")
    }

    fun getDistricts () = viewModelScope.launch {
        _district.value = Resource.Loading<Array<String>>()

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let { districtRes ->
                if(districtRes.isSuccess) _district.value = Resource.Success<Array<String>>(districtRes.result.map { it.name }.toTypedArray())
                else _district.value = Resource.Error<Array<String>>(null, "지역 가져오기를 실패했습니다.")
            }
        } else _district.value = Resource.Error<Array<String>>(null, "지역 가져오기를 실패했습니다.")

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

    private fun isEndTimeFasterThanStart(startTime: String, endTime: String): Boolean {

        val startHourString = startTime.split(":")[0]
        val startMinString = startTime.split(":")[1]

        val startHourInt = startHourString.toInt()
        val startMinInt = startMinString.toInt()

        val endHourString = endTime.split(":")[0]
        val endMinString = endTime.split(":")[1]

        val endHourInt = endHourString.toInt()
        val endMinInt = endMinString.toInt()

        return startHourInt > endHourInt || startHourInt >= endHourInt && startMinInt >= endMinInt

    }


}