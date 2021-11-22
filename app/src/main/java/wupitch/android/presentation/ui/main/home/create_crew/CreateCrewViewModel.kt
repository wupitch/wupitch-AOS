package wupitch.android.presentation.ui.main.home.create_crew

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    private val getDistrictRepository: GetDistrictRepository
) : ViewModel() {

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _crewSportId = mutableStateOf(-1)
    val crewSportId: State<Int> = _crewSportId

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _crewDistrictId = MutableLiveData<Int>()
    val crewDistrictId: LiveData<Int> = _crewDistrictId

    private var _crewDistrictName = MutableLiveData<String>()
    val crewDistrictName: LiveData<String> = _crewDistrictName

    private var _crewLocation = mutableStateOf("")
    val crewLocation: State<String> = _crewLocation

    private var _crewName = mutableStateOf("")
    val crewName: State<String> = _crewName

    private var _crewSize = mutableStateOf("")
    val crewSize: State<String> = _crewSize

    private var _crewAgeGroupList = mutableStateListOf<Int>()
    val crewAgeGroupList: SnapshotStateList<Int> = _crewAgeGroupList

    private var _crewExtraInfoList = mutableStateListOf<Int>()
    val crewExtraInfoList: SnapshotStateList<Int> = _crewExtraInfoList

    private var _crewTitle = mutableStateOf("")
    val crewTitle : State<String> = _crewTitle

    private var _crewIntro = mutableStateOf("")
    val crewIntro : State<String> = _crewIntro

    private var _crewSupplies = mutableStateOf("")
    val crewSupplies : State<String> = _crewSupplies

    private var _crewInquiry = mutableStateOf("")
    val crewInquiry : State<String> = _crewInquiry


    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage: State<Boolean?> = _isUsingDefaultImage

    private var _imageChosenState = mutableStateOf(false)
    val imageChosenState : State<Boolean> = _imageChosenState




    private var _scheduleList = mutableStateListOf<ScheduleState>(
        ScheduleState(
            day = mutableStateOf(-1),
            startTime = mutableStateOf("00:00"),
            endTime = mutableStateOf("00:00"),
            isStartTimeSet = mutableStateOf<Boolean?>(false),
            isEndTimeSet = mutableStateOf<Boolean?>(false)
        )
    )
    val scheduleList: SnapshotStateList<ScheduleState> = _scheduleList


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
                        if (_crewSportId.value == index) {
                            filterItem.state.value = true
                        }
                    }
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

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

    fun setCrewSport(sportId: Int) {
        _crewSportId.value = sportId
        Log.d("{CreateCrewViewModel.setCrewSport}", _crewSportId.value.toString())
    }

    fun setCrewDistrict(districtId: Int, districtName: String) {
        //todo 서버에 보내기
        _crewDistrictId.value = districtId
        _crewDistrictName.value = districtName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $districtId name : $districtName")
    }

    fun setCrewLocation(location: String) {
        //todo 장소 미정이면 서버한테 null or 장소미정  text 보내야 되나?
        _crewLocation.value = location
        Log.d("{CreateCrewViewModel.setCrewLocation}", _crewLocation.value)
    }

    fun setCrewName(name: String) {
        _crewName.value = name
    }

    fun setCrewSize(size: String) {
        _crewSize.value = size
    }

    fun setCrewAgeGroupList(list: SnapshotStateList<Int>) {
        _crewAgeGroupList = list
        Log.d("{CreateCrewViewModel.setCrewAgeGroupList}", _crewAgeGroupList.toList().toString())
    }

    fun setCrewExtraInfoList(list: SnapshotStateList<Int>) {
        _crewExtraInfoList = list
        Log.d("{CreateCrewViewModel.setCrewExtraInfoList}", _crewExtraInfoList.toList().toString())
    }

    fun addCrewSchedule() {
        _scheduleList.add(
            ScheduleState(
                day = mutableStateOf(-1),
                startTime = mutableStateOf("00:00"),
                endTime = mutableStateOf("00:00"),
                isStartTimeSet = mutableStateOf<Boolean?>(false),
                isEndTimeSet = mutableStateOf<Boolean?>(false)
            )
        )
    }

    fun setImageSource(isUsingDefaultImage: Boolean?) {
        _isUsingDefaultImage.value = isUsingDefaultImage
    }

    fun setImageChosenState(isImageChosen: Boolean) {
        _imageChosenState.value = isImageChosen
    }

    fun setTimeFilter(index: Int, type: TimeType, hour: Int, min: Int) {
        Log.d("{CreateCrewViewModel.setTimeFilter}", "hour : $hour min : $min")
        var hourString = hour.toString()
        var minString = min.toString()
        if (hour < 10) hourString = "0$hour"
        if (min < 10) minString = "0$min"
        val timeString = "$hourString:$minString"

        val schedule = scheduleList[index]

        when (type) {
            TimeType.START -> {
                if (schedule.isEndTimeSet.value == true) {
                    if (isEndTimeFasterThanStart(timeString, schedule.endTime.value)) {
                        schedule.startTime.value = "00:00"
                        schedule.isStartTimeSet.value = null
                    } else {
                        schedule.startTime.value = timeString
                        schedule.isStartTimeSet.value = true
                    }
                } else {
                    schedule.startTime.value = timeString
                    schedule.isStartTimeSet.value = true
                }
            }
            TimeType.END -> {

                if (isEndTimeFasterThanStart(schedule.startTime.value, timeString)) {
                    schedule.endTime.value = "00:00"
                    schedule.isEndTimeSet.value = null
                } else {
                    schedule.endTime.value = timeString
                    schedule.isEndTimeSet.value = true
                }
            }
        }
    }

    fun setCrewTitle(title : String) {
        _crewTitle.value = title
    }

    fun setCrewIntro(intro : String) {
        _crewIntro.value = intro
    }
    fun setCrewSupplies(supplies : String) {
        _crewSupplies.value = supplies
    }
    fun setCrewInquiry(inquiry : String) {
        _crewInquiry.value = inquiry
    }

    private var _crewFee = mutableStateOf<String>("")
    val crewFee : State<String> = _crewFee

    private var _noCrewFee = mutableStateOf(false)
    val noCrewFee : State<Boolean> = _noCrewFee

    private var _crewVisitorFee = mutableStateOf<String>("")
    val crewVisitorFee : State<String> = _crewVisitorFee

    private var _noCrewVisitorFee = mutableStateOf(false)
    val noCrewVisitorFee: State<Boolean> = _noCrewVisitorFee

    fun setCrewFee(money : String, toggleState : Boolean) {
        _crewFee.value = money
        _noCrewFee.value = toggleState
    }

    fun setCrewVisitorFee(money : String, toggleState : Boolean) {
        _crewVisitorFee.value = money
        _noCrewVisitorFee.value = toggleState
    }
}