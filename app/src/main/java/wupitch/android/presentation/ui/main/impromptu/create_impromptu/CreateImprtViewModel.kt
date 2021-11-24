package wupitch.android.presentation.ui.main.impromptu.create_impromptu

import android.net.Uri
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
import wupitch.android.common.Constants
import wupitch.android.common.Constants.EMPTY_IMAGE_URI
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.Schedule
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.home.create_crew.ScheduleState
import wupitch.android.util.TimeType
import wupitch.android.util.isEndTimeFasterThanStart
import wupitch.android.util.wonToNum
import javax.inject.Inject

@HiltViewModel
class CreateImprtViewModel @Inject constructor(
    private val getDistrictRepository: GetDistrictRepository,
    private val crewRepository : CrewRepository
) : ViewModel() {

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _imprtDistrictId = MutableLiveData<Int>()
    val imprtDistrictId: LiveData<Int> = _imprtDistrictId

    private var _imprtDistrictName = MutableLiveData<String>()
    val imprtDistrictName: LiveData<String> = _imprtDistrictName

    private var _imprtLocation = mutableStateOf("")
    val crewLocation: State<String> = _imprtLocation

    private var _imprtSize = mutableStateOf("")
    val imprtSize: State<String> = _imprtSize


    private var _imprtTitle = mutableStateOf("")
    val imprtTitle : State<String> = _imprtTitle

    private var _imprtIntro = mutableStateOf("")
    val imprtIntro : State<String> = _imprtIntro

    private var _imprtSupplies = mutableStateOf("")
    val imprtSupplies : State<String> = _imprtSupplies

    private var _imprtInquiry = mutableStateOf("")
    val imprtInquiry : State<String> = _imprtInquiry


    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage: State<Boolean?> = _isUsingDefaultImage

    private var _imageChosenState = mutableStateOf(false)
    val imageChosenState : State<Boolean> = _imageChosenState

    private var _imprtImage = mutableStateOf<Uri?>(Constants.EMPTY_IMAGE_URI)
    val imprtImage : State<Uri?> = _imprtImage

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

    private var _createImprtState = mutableStateOf(CreateImpromptuState())
    val createImprtState : State<CreateImpromptuState> = _createImprtState



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


    fun setImprtDistrict(districtId: Int, districtName: String) {
        _imprtDistrictId.value = districtId
        _imprtDistrictName.value = districtName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $districtId name : $districtName")
    }

    fun setImprtLocation(location: String) {
        _imprtLocation.value = location
        Log.d("{CreateCrewViewModel.setCrewLocation}", _imprtLocation.value)
    }


    fun setImprtSize(size: String) {
        _imprtSize.value = size
    }


    fun addImprtSchedule() {
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

    fun setImprtImage(image : Uri) {
        _imprtImage.value = image
    }

    fun setIsUsingDefaultImage(isUsingDefaultImage: Boolean?) {
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

    fun setImprtTitle(title : String) {
        _imprtTitle.value = title
    }

    fun setImprtIntro(intro : String) {
        _imprtIntro.value = intro
    }
    fun setImprtSupplies(supplies : String) {
        _imprtSupplies.value = supplies
    }
    fun setImprtInquiry(inquiry : String) {
        _imprtInquiry.value = inquiry
    }

    private var _imprtFee = mutableStateOf<String>("")
    val imprtFee : State<String> = _imprtFee

    private var _noImprtFee = mutableStateOf(false)
    val noImprtFee : State<Boolean> = _noImprtFee


    fun setImprtFee(money : String, toggleState : Boolean) {
        _imprtFee.value = money
        _noImprtFee.value = toggleState
    }

    fun createImpromptu() = viewModelScope.launch {
//        _createImprtState.value = CreateImpromptuState(isLoading = true)

//        val crewReq = CreateCrewReq(
//            areaId =  _imprtDistrictId.value!! +1,
//            conference = if(_noImprtFee.value) null else _imprtFee.value.wonToNum(),
//            inquiries = _imprtInquiry.value,
//            materials = if(_imprtSupplies.value.isNotEmpty()) _imprtSupplies.value else null,
//            introduction = _imprtIntro.value,
//            location = if(_imprtLocation.value.isEmpty()) null else _imprtLocation.value,
//            title = _imprtTitle.value,
//            memberCount = _imprtSize.value.toInt(),
//            scheduleList = _scheduleList.map { Schedule(it.day.value +1, it.startTime.value.stringToDouble(), it.endTime.value.stringToDouble()) }
//        )
//        val response = crewRepository.createCrew(crewReq)
//        if(response.isSuccessful) {
//            response.body()?.let { res ->
//                if(res.isSuccess) postCrewImage(res.result.clubId)
//                else _createImprtState.value = CreateImpromptuState(error = res.message)
//            }
//        }else _createImprtState.value = CreateImpromptuState(error = "크루 생성을 실패했습니다.")
    }

    private fun postCrewImage(crewId : Int) = viewModelScope.launch {
//        Log.d("{CreateCrewViewModel.postCrewImage}", "${_isUsingDefaultImage.value}, ${_imprtImage.value}")
//        if(_imprtImage.value == EMPTY_IMAGE_URI) {
//            _createImprtState.value = CreateImpromptuState(data = crewId)
//        }else{
//            val response = crewRepository.postCrewImage(_imprtImage.value.toString(), crewId)
//            if(response.isSuccessful){
//                response.body()?.let { res ->
//                    if(res.isSuccess)  _createImprtState.value = CreateImpromptuState(data = crewId)
//                    else _createImprtState.value = CreateImpromptuState(error = res.message)
//                }
//            }else _createImprtState.value = CreateImpromptuState(error = "크루 이미지 업로드를 실패했습니다.")
//        }
    }
}