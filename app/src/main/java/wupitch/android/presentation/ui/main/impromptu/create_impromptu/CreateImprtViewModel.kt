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
import wupitch.android.domain.model.CreateImprtReq
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.home.create_crew.ScheduleState
import wupitch.android.util.*
import javax.inject.Inject

@HiltViewModel
class CreateImprtViewModel @Inject constructor(
    private val getDistrictRepository: GetDistrictRepository,
    private val imprtRepository: ImprtRepository
) : ViewModel() {

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _imprtDistrictId = MutableLiveData<Int>()
    val imprtDistrictId: LiveData<Int> = _imprtDistrictId

    private var _imprtDistrictName = MutableLiveData<String>()
    val imprtDistrictName: LiveData<String> = _imprtDistrictName

    private var _imprtLocation = mutableStateOf("")
    val crewLocation: State<String> = _imprtLocation

    private var _imprtSize = mutableStateOf("00")
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

    private var _dateState = mutableStateOf("0000.00.00 (요일)")
    val dateState : State<String> = _dateState

    private var _dateValidState = mutableStateOf<Boolean?>(true)
    val dateValidState : State<Boolean?> = _dateValidState

    private var _timeState = mutableStateOf<TimeState>(
        TimeState(
            startTime = mutableStateOf("00:00"),
            endTime = mutableStateOf("00:00"),
            isStartTimeSet = mutableStateOf<Boolean?>(false),
            isEndTimeSet = mutableStateOf<Boolean?>(false)
        )
    )
    val timeState: State<TimeState> = _timeState

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

    fun setDateState(longDate : Long?){
        if(longDate != null){
            val date = dateFormatter(longDate)
            if( date != null){
                _dateState.value = date
                _dateValidState.value = true
            }else {
                _dateState.value = "0000.00.00 (요일)"
                _dateValidState.value = false
            }
        }else {
            _dateState.value = "0000.00.00 (요일)"
        }

    }

    fun resetDateValid() {
        _dateValidState.value = null
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

    fun setTimeFilter(type: TimeType, hour: Int, min: Int) {
        Log.d("{CreateCrewViewModel.setTimeFilter}", "hour : $hour min : $min")
        var hourString = hour.toString()
        var minString = min.toString()
        if (hour < 10) hourString = "0$hour"
        if (min < 10) minString = "0$min"
        val timeString = "$hourString:$minString"


        when (type) {
            TimeType.START -> {
                if (_timeState.value.isEndTimeSet.value == true) {
                    if (isEndTimeFasterThanStart(timeString, _timeState.value.endTime.value)) {
                        _timeState.value.startTime.value = "00:00"
                        _timeState.value.isStartTimeSet.value = null
                    } else {
                        _timeState.value.startTime.value = timeString
                        _timeState.value.isStartTimeSet.value = true
                    }
                } else {
                    _timeState.value.startTime.value = timeString
                    _timeState.value.isStartTimeSet.value = true
                }
            }
            TimeType.END -> {
                if (isEndTimeFasterThanStart(_timeState.value.startTime.value, timeString)) {
                    _timeState.value.endTime.value = "00:00"
                    _timeState.value.isEndTimeSet.value = null
                } else {
                    _timeState.value.endTime.value = timeString
                    _timeState.value.isEndTimeSet.value = true
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
        _createImprtState.value = CreateImpromptuState(isLoading = true)

        val req = CreateImprtReq(
            areaId =  _imprtDistrictId.value!! +1,
            dues = if(_noImprtFee.value) null else _imprtFee.value.wonToNum(),
            inquiries = _imprtInquiry.value,
            materials = if(_imprtSupplies.value.isNotEmpty()) _imprtSupplies.value else null,
            introduction = _imprtIntro.value,
            location = if(_imprtLocation.value.isEmpty()) null else _imprtLocation.value,
            title = _imprtTitle.value,
            recruitmentCount = _imprtSize.value.toInt(),
            date = convertDateForServer(_dateState.value),
            startTime = _timeState.value.startTime.value.stringToDouble(),
            endTime = _timeState.value.endTime.value.stringToDouble()
        )
        val response = imprtRepository.createImprt(req)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _createImprtState.value = CreateImpromptuState(data = res.result.impromptuId) //postCrewImage(res.result.clubId)
                else _createImprtState.value = CreateImpromptuState(error = res.message)
            }
        }else _createImprtState.value = CreateImpromptuState(error = "번개 생성을 실패했습니다.")
    }

    private fun postImprtImage(id : Int) = viewModelScope.launch {
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

    private fun convertDateForServer(date : String) : String {
        val datePart = date.split(" ")[0]
        return datePart.replace(".", "-")
    }
}