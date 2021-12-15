package wupitch.android.presentation.ui.main.home.create_crew

import android.net.Uri
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
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.Schedule
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.util.*
import java.io.File
import javax.inject.Inject


@HiltViewModel
class CreateCrewViewModel @Inject constructor(
    private val getSportRepository: GetSportRepository,
    private val getDistrictRepository: GetDistrictRepository,
    private val crewRepository: CrewRepository,
    private val getImageFile: GetImageFile,
) : ViewModel() {

    /*
       * info (name, size, age group, extra info, intro, supply, inquiry)
       * */

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
    val crewTitle: State<String> = _crewTitle

    private var _crewIntro = mutableStateOf("")
    val crewIntro: State<String> = _crewIntro

    private var _crewSupplies = mutableStateOf("")
    val crewSupplies: State<String> = _crewSupplies

    private var _crewInquiry = mutableStateOf("")
    val crewInquiry: State<String> = _crewInquiry

    fun setCrewTitle(title: String) {
        _crewTitle.value = title
    }

    fun setCrewLocation(location: String) {
        _crewLocation.value = location
    }

    fun setCrewName(name: String) {
        _crewName.value = name
    }

    fun setCrewSize(size: String) {
        _crewSize.value = size
    }

    fun setCrewAgeGroupList(list: SnapshotStateList<Int>) {
        _crewAgeGroupList = list
    }

    fun setCrewExtraInfoList(list: SnapshotStateList<Int>) {
        _crewExtraInfoList = list
    }

    fun setCrewIntro(intro: String) {
        _crewIntro.value = intro
    }

    fun setCrewSupplies(supplies: String) {
        _crewSupplies.value = supplies
    }

    fun setCrewInquiry(inquiry: String) {
        _crewInquiry.value = inquiry
    }

    /*
    * schedule
    * */

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

    fun setTimeFilter(index: Int, type: TimeType, hour: Int, min: Int) {
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




    /*
    * sports
    * */

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _crewSportId = mutableStateOf(-1)
    val crewSportId: State<Int> = _crewSportId

    fun setCrewSport(sportId: Int) {
        _crewSportId.value = sportId
    }

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

    /*
    * district
    * */

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _crewDistrictId = MutableLiveData<Int>()
    val crewDistrictId: LiveData<Int> = _crewDistrictId

    private var _crewDistrictName = MutableLiveData<String>()
    val crewDistrictName: LiveData<String> = _crewDistrictName

    fun setCrewDistrict(districtId: Int, districtName: String) {
        _crewDistrictId.value = districtId
        _crewDistrictName.value = districtName
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


    /*
    * fee
    * */

    private var _crewFee = mutableStateOf<String>("")
    val crewFee: State<String> = _crewFee

    private var _noCrewFee = mutableStateOf(false)
    val noCrewFee: State<Boolean> = _noCrewFee

    private var _crewVisitorFee = mutableStateOf<String>("")
    val crewVisitorFee: State<String> = _crewVisitorFee

    private var _noCrewVisitorFee = mutableStateOf(false)
    val noCrewVisitorFee: State<Boolean> = _noCrewVisitorFee

    fun setCrewFee(money: String, toggleState: Boolean) {
        _crewFee.value = money
        _noCrewFee.value = toggleState
    }

    fun setCrewVisitorFee(money: String, toggleState: Boolean) {
        _crewVisitorFee.value = money
        _noCrewVisitorFee.value = toggleState
    }

    /*
    * create crew
    * */
    private var _createCrewState = mutableStateOf(CreateCrewState())
    val createCrewState: State<CreateCrewState> = _createCrewState

    fun createCrew() = viewModelScope.launch {
        _createCrewState.value = CreateCrewState(isLoading = true)

        val crewReq = CreateCrewReq(
            ageList = _crewAgeGroupList.map { it + 1 },
            areaId = _crewDistrictId.value!! + 1,
            crewName = _crewName.value,
            conference = if (_noCrewFee.value) null else _crewFee.value.wonToNum(),
            extraInfoList = _crewExtraInfoList.map { it + 1 },
            guestConference = if (_noCrewVisitorFee.value) null else _crewVisitorFee.value.wonToNum(),
            inquiries = _crewInquiry.value,
            materials = if (_crewSupplies.value.isNotEmpty()) _crewSupplies.value else null,
            introduction = _crewIntro.value,
            location = if (_crewLocation.value.isEmpty()) null else _crewLocation.value,
            sportsId = _crewSportId.value + 1,
            title = _crewTitle.value,
            memberCount = _crewSize.value.toInt(),
            scheduleList = _scheduleList.map {
                Schedule(
                    dayIdx  = it.day.value + 1,
                    startTime =  it.startTime.value.stringToDouble(),
                    endTime = it.endTime.value.stringToDouble()
                )
            }
        )
        val response = crewRepository.createCrew(crewReq)
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) postCrewImage(res.result.clubId)
                else _createCrewState.value = CreateCrewState(error = res.message)
            }
        } else _createCrewState.value = CreateCrewState(error = "크루 생성을 실패했습니다.")
    }

    /*
    * image
    * */

    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage: State<Boolean?> = _isUsingDefaultImage

    private var _imageChosenState = mutableStateOf(false)
    val imageChosenState: State<Boolean> = _imageChosenState

    private var _crewImage = mutableStateOf<Uri>(Constants.EMPTY_IMAGE_URI)
    val crewImage: State<Uri> = _crewImage

    fun setCrewImage(image: Uri) {
        _crewImage.value = image
    }

    fun setIsUsingDefaultImage(isUsingDefaultImage: Boolean?) {
        _isUsingDefaultImage.value = isUsingDefaultImage
    }

    fun setImageChosenState(isImageChosen: Boolean) {
        _imageChosenState.value = isImageChosen
    }

    private fun postCrewImage(crewId: Int) = viewModelScope.launch {

        if (_crewImage.value == Constants.EMPTY_IMAGE_URI) {
            _createCrewState.value = CreateCrewState(data = crewId)
        } else {
            val file = getImageFile.getImageFile(_crewImage.value)

            if (file != null) {
                val multipartBodyPart = getImageBody(file)

                val response = crewRepository.postCrewImage(multipartBodyPart.body, multipartBodyPart, crewId)
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        if (res.isSuccess) _createCrewState.value = CreateCrewState(data = crewId)
                        else _createCrewState.value = CreateCrewState(error = res.message)
                    }
                } else _createCrewState.value = CreateCrewState(error = "크루 이미지 업로드를 실패했습니다.")
            }
        }
    }

}