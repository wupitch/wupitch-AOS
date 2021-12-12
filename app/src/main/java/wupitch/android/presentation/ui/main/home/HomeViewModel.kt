package wupitch.android.presentation.ui.main.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wupitch.android.CrewFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants.PAGE_SIZE
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.toCrewCardInfo
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository,
    private val crewRepository: CrewRepository,
    private val crewFilterDataStore : DataStore<CrewFilter>
) : ViewModel() {

    /*
    * scroll
    * */
    var firstVisibleItemIndex  = 0
    var firstVisibleItemOffset = 0

    fun saveScrollPosition(itemIdx : Int, offset : Int){
        firstVisibleItemIndex = itemIdx
        firstVisibleItemOffset = offset
    }

    /*
    * pagination
    * */

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")

    private var _page = mutableStateOf(1)
    val page : State<Int> = _page

    fun resetPage () {
        _page.value = 1
    }
    private fun incrementPage() {
        _page.value = _page.value +1
    }

    private var scrollPosition = 0

    fun onChangeScrollPosition(position : Int) {
        scrollPosition = position
    }

    private fun appendList(list : List<CrewCardInfo>) {
        _crewState.addAll(list)
    }

    fun getNewPage() = viewModelScope.launch {
        if((scrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()

            if(page.value >1){
                getCrew()
            }
        }
    }



    /*
    * filter
    * */

    //event
    private var _crewEventList = mutableStateListOf<Int>()
    val crewEventList: SnapshotStateList<Int> = _crewEventList

    fun setCrewEventList(list: SnapshotStateList<Int>) {
        _crewEventList = list
    }
    //day
    private var _crewDayList = mutableStateListOf<Int>()
    val crewDayList: SnapshotStateList<Int> = _crewDayList

    fun setCrewDayList(list: SnapshotStateList<Int>) {
        _crewDayList = list
    }
    //age
    private var _crewAgeGroupList = mutableStateListOf<Int>()
    val crewAgeGroupList: SnapshotStateList<Int> = _crewAgeGroupList

    fun setCrewAgeGroupList(list: SnapshotStateList<Int>) {
        _crewAgeGroupList = list
    }
    //size
    private var _crewSizeState = mutableStateOf<Int?>(null)
    val crewSizeState : State<Int?> = _crewSizeState

    fun setCrewSize(size: Int?) {
        _crewSizeState.value = size
    }

    private var _resetState = mutableStateOf(false)
    val resetState : State<Boolean> = _resetState

    fun resetFilter() {
        _resetState.value = true
        _crewEventList.clear()
        _crewDayList.clear()
        _crewAgeGroupList.clear()
        _userDistrictId.value = null
        _userDistrictName.value = "서울시"
        _crewSizeState.value = null
        resetPage()
    }

    var filterApplied = mutableStateOf(false)
    fun applyFilter() = viewModelScope.launch {
        resetPage()
        crewFilterDataStore.updateData {
            it.toBuilder()
                .setSize(_crewSizeState.value ?: -1)
                .clearAgeList().addAllAgeList(_crewAgeGroupList)
                .clearDayList().addAllDayList(_crewDayList)
                .clearSportList().addAllSportList(_crewEventList)
                .setAreaId(_userDistrictId.value ?: 0) //area id, name 초기화 후에 적용하려면 저장 필요함.
                .setAreaName(_userDistrictName.value)
                .build()
        }
        saveScrollPosition(0,0)
        filterApplied.value = true
    }

    private val _getCrewFilterState = mutableStateOf(BaseState())
    var getCrewFilterState : State<BaseState> = _getCrewFilterState


    fun getCrewFilter() = viewModelScope.launch { //filter view 에서 부름.
        loading.value = true
        val crewFilter = crewFilterDataStore.data.first()

        crewFilter.ageListList.forEach {
            if (!_crewAgeGroupList.contains(it)) _crewAgeGroupList.add(it)
        }
        crewFilter.dayListList.forEach {
            if (!_crewDayList.contains(it)) _crewDayList.add(it)
        }
        crewFilter.sportListList.forEach {
            if (!_crewEventList.contains(it)) _crewEventList.add(it)
        }
        _crewSizeState.value = if(crewFilter.size == -1) null else crewFilter.size
        loading.value = false
    }

    /*
    * get crew
    * */

    private var _crewState = mutableStateListOf<CrewCardInfo>()
    val crewState : SnapshotStateList<CrewCardInfo> = _crewState

    fun getCrew() = viewModelScope.launch {
        loading.value = true
        val crewFilter = crewFilterDataStore.data.first()

        //지역구 필터만 홈에서 세팅. 나머지는 필터뷰에서.
        _userDistrictName.value = if(crewFilter.areaName.isEmpty())"서울시" else crewFilter.areaName

        //필터 읽어서 그대로 get crew.
        val response = crewRepository.getCrew(
            ageList = if(crewFilter.ageListList.isEmpty())null else crewFilter.ageListList.map { it +1 },
            areaId = crewFilter.areaId +1,
            days = if(crewFilter.dayListList.isEmpty())null else crewFilter.dayListList.map { it+1 },
            memberCountValue = if(crewFilter.size == -1) null else crewFilter.size+1,
            page = _page.value,
            sportsList = if(crewFilter.sportListList.isEmpty())null else crewFilter.sportListList.map { it + 1 }
        )
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    if(res.result.first) _crewState.clear()
                    appendList(res.result.content.map { it.toCrewCardInfo() })
                } else {
                    error.value = res.message
                }
            }
        }else error.value = "크루 조회에 실패했습니다."
        loading.value = false
    }

    /*
    * district
    * */

    private var _districtList = mutableStateOf(DistrictState())
    val districtList : State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)

    private var _userDistrictName = mutableStateOf<String>("서울시")
    val userDistrictName : State<String> = _userDistrictName

    fun setUserDistrict(districtId : Int, districtName : String) = viewModelScope.launch {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        crewFilterDataStore.updateData {
            it.toBuilder()
                .setAreaName(_userDistrictName.value)
                .setAreaId(_userDistrictId.value!!)
                .build()
        }
        resetPage()
        saveScrollPosition(0,0)
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