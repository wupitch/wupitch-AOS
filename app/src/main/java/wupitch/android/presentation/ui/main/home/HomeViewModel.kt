package wupitch.android.presentation.ui.main.home

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.Constants.PAGE_SIZE
import wupitch.android.common.Resource
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository,
    private val crewRepository: CrewRepository
) : ViewModel() {


    val loading = mutableStateOf(false)
    val error = mutableStateOf("")

    val page = mutableStateOf(1)
    private var scrollPosition = 0

    private var _crewState = mutableStateListOf<CrewCardInfo>()
    val crewState : SnapshotStateList<CrewCardInfo> = _crewState

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList : State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)

    private var _userDistrictName = mutableStateOf<String>("지역구")
    val userDistrictName : State<String> = _userDistrictName

    //event
    private var _crewEventList = mutableStateListOf<Int>()
    val crewEventList: SnapshotStateList<Int> = _crewEventList

    //day
    private var _crewDayList = mutableStateListOf<Int>()
    val crewDayList: SnapshotStateList<Int> = _crewDayList

    //age
    private var _crewAgeGroupList = mutableStateListOf<Int>()
    val crewAgeGroupList: SnapshotStateList<Int> = _crewAgeGroupList

    //size
    private var _crewSizeState = mutableStateOf<Int?>(null)
    val crewSizeState : State<Int?> = _crewSizeState

    /*
    * pagination
    * */

    private fun appendList(list : List<CrewCardInfo>) {
        _crewState.addAll(list)
    }

    fun getNewPage() = viewModelScope.launch {
        if((scrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d("{HomeViewModel.newPage}", "${page.value}")

            if(page.value >1){
                getCrew()
            }
        }
    }

    private fun incrementPage() {
        page.value = page.value +1
    }

    fun onChangeScrollPosition(position : Int) {
        scrollPosition = position
    }

    fun setCrewAgeGroupList(list: SnapshotStateList<Int>) {
        _crewAgeGroupList = list
        Log.d("{CreateCrewViewModel.setCrewAgeGroupList}", _crewAgeGroupList.toList().toString())
    }

    fun setCrewEventList(list: SnapshotStateList<Int>) {
        _crewEventList = list
        Log.d("{CreateCrewViewModel.setCrewEventList}", _crewEventList.toList().toString())
    }

    fun setCrewDayList(list: SnapshotStateList<Int>) {
        _crewDayList = list
        Log.d("{CreateCrewViewModel.setCrewDayList}", _crewDayList.toList().toString())
    }

    fun setCrewSize(size: Int?) {
        _crewSizeState.value = size
    }

    fun applyFilter() {
        //todo save in data store
        getCrew()
    }

    fun resetFilter() {
        //todo
    }


    fun getCrew() = viewModelScope.launch {
        loading.value = true

        val response = crewRepository.getCrew(
            ageList = if(_crewAgeGroupList.isEmpty())null else _crewAgeGroupList.map { it +1 },
            areaId = if(_userDistrictId.value == null) null else _userDistrictId.value!! +1,
            days = if(_crewDayList.isEmpty())null else _crewDayList.map { it+1 },
            memberCountValue = _crewSizeState.value,
            page = page.value,
            sportsList = if(_crewEventList.isEmpty())null else _crewEventList.map { it + 1 }
        )
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    loading.value = false
                    appendList(res.result.content.map {
                        CrewCardInfo(
                            id = it.clubId,
                            sportId = it.sportsId -1,
                            isPinned = it.isPinUp,
                            time = "${it.schedules[0].day} ${doubleToTime(it.schedules[0].startTime)}-${doubleToTime(it.schedules[0].endTime)}",
                            isMoreThanOnceAWeek = it.schedules.size >1,
                            detailAddress = it.areaName ?: "장소 미정",
                            crewImage = it.crewImage,
                            title = it.clubTitle
                        )})
                } else {
                    error.value = res.message
                }
            }
        }else error.value = "크루 조회에 실패했습니다."
    }


    fun setUserDistrict(districtId : Int, districtName : String) {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        getCrew()
    }

    fun getDistricts () = viewModelScope.launch {
        _districtList.value = DistrictState(isLoading = true)

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let { districtRes ->
                if(districtRes.isSuccess) _districtList.value = DistrictState(data = districtRes.result.map { it.name }.toTypedArray())
                else _districtList.value = DistrictState( error = districtRes.message)
            }
        } else _districtList.value =  DistrictState( error = "지역 가져오기를 실패했습니다.")
    }



}