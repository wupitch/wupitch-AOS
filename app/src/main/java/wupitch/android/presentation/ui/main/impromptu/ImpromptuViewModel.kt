package wupitch.android.presentation.ui.main.impromptu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wupitch.android.ImpromptuFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.data.remote.dto.toImprtCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class ImpromptuViewModel @Inject constructor(
    private val getDistrictRepository: GetDistrictRepository,
    private val imprtRepository: ImprtRepository,
    private val imprtFilterDataStore: DataStore<ImpromptuFilter>
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
    val page: State<Int> = _page

    fun resetPage() {
        _page.value = 1
    }
    private fun incrementPage() {
        _page.value = _page.value + 1
    }

    private var scrollPosition = 0

    fun onChangeScrollPosition(position: Int) {
        scrollPosition = position
    }

    private fun appendList(list: List<ImpromptuCardInfo>) {
        _imprtState.addAll(list)
    }

    fun getNewPage() = viewModelScope.launch {
        if ((scrollPosition + 1) >= (page.value * Constants.PAGE_SIZE)) {
            incrementPage()

            if (page.value > 1) {
                getImprt()
            }
        }
    }


    /*
    * filter
    * */

    //day
    private var _imprtDayList = mutableStateListOf<Int>()
    val imprtDayList: SnapshotStateList<Int> = _imprtDayList

    fun setImprtDayList(list: SnapshotStateList<Int>) {
        _imprtDayList = list
    }


    //size
    private var _imprtSizeState = mutableStateOf<Int?>(null)
    val imprtSizeState: State<Int?> = _imprtSizeState

    fun setImprtSize(size: Int?) {
        _imprtSizeState.value = size

    }

    //schedule
    private var _imprtScheduleState = mutableStateOf<Int?>(null)
    val imprtScheduleState: State<Int?> = _imprtScheduleState

    fun setImprtSchedule(size: Int?) {
        _imprtScheduleState.value = size
    }

    private var _resetState = mutableStateOf(false)
    val resetState: State<Boolean> = _resetState

    fun resetFilter() {
        _resetState.value = true
        _imprtDayList.clear()
        _userDistrictId.value = null
        _userDistrictName.value = "서울시"
        _imprtScheduleState.value = null
        _imprtSizeState.value = null
        resetPage()
    }

    var filterApplied = mutableStateOf(false)

    fun applyFilter() = viewModelScope.launch {
        resetPage()
        imprtFilterDataStore.updateData {
            it.toBuilder()
                .setSchedule(_imprtScheduleState.value ?: -1)
                .clearDays().addAllDays(_imprtDayList)
                .setRecruitSize(_imprtSizeState.value ?: -1)
                .setAreaId(_userDistrictId.value ?: 0) //area id, name 초기화 후에 적용하려면 저장 필요함.
                .setAreaName(_userDistrictName.value)
                .build()
        }
        saveScrollPosition(0,0)
        filterApplied.value = true
    }

    private val _getImprtFilterState = mutableStateOf(BaseState())
    var getImprtFilterState: State<BaseState> = _getImprtFilterState


    fun getImprtFilter() = viewModelScope.launch {
        loading.value = true

        val imprtFilter = imprtFilterDataStore.data.first()

        imprtFilter.daysList.forEach {
            if (!_imprtDayList.contains(it)) _imprtDayList.add(it)
        }
        _imprtSizeState.value = if(imprtFilter.recruitSize == -1) null else imprtFilter.recruitSize
        _imprtScheduleState.value = if(imprtFilter.schedule == -1) null else imprtFilter.schedule

        loading.value = false
    }

    /*
    * get impromptu
    * */

    private var _imprtState = mutableStateListOf<ImpromptuCardInfo>()
    val imprtState: SnapshotStateList<ImpromptuCardInfo> = _imprtState


    fun getImprt() = viewModelScope.launch {
        loading.value = true
        val imprtFilter = imprtFilterDataStore.data.first()

        //지역구 필터만 번개탭에서 세팅. 나머지는 필터뷰에서.
        _userDistrictName.value = if(imprtFilter.areaName.isEmpty())"서울시" else imprtFilter.areaName

        //필터 읽어서 그대로 get impromptu.
        val response = imprtRepository.getImpromptu(
            areaId = imprtFilter.areaId +1,
            days = if (imprtFilter.daysList.isEmpty()) null else imprtFilter.daysList.map { it+1 },
            memberCountIndex = if (imprtFilter.recruitSize == -1) null else imprtFilter.recruitSize +1,
            page = _page.value,
            scheduleIndex = if (imprtFilter.schedule == -1) null else imprtFilter.schedule + 1
        )

        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    if (res.result.first) _imprtState.clear()
                    appendList(res.result.content.map { it.toImprtCardInfo() })
                } else {
                    error.value = res.message
                }
            }
        } else error.value = "번개 조회에 실패했습니다."

        loading.value = false
    }


    /*
    * districts
    * */

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)

    private var _userDistrictName = mutableStateOf<String>("서울시")
    val userDistrictName: State<String> = _userDistrictName

    fun setUserDistrict(districtId: Int, districtName: String) = viewModelScope.launch {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        imprtFilterDataStore.updateData {
            it.toBuilder()
                .setAreaName(_userDistrictName.value)
                .setAreaId(_userDistrictId.value!!)
                .build()
        }
        resetPage()
        saveScrollPosition(0,0)
        getImprt()
    }


    fun getDistricts() = viewModelScope.launch {
        _districtList.value = DistrictState(isLoading = true)

        val response = getDistrictRepository.getDistricts()
        if (response.isSuccessful) {
            response.body()?.let { districtRes ->
                if (districtRes.isSuccess) _districtList.value =
                    DistrictState(data = districtRes.result.map { it.name }.toTypedArray())
                else _districtList.value = DistrictState(error = districtRes.message)
            }
        } else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")
    }

}