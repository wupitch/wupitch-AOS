package wupitch.android.presentation.ui.main.impromptu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Resource
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.home.CrewState
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.my_activity.ImprtState
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class ImpromptuViewModel @Inject constructor(
    private val getDistrictRepository: GetDistrictRepository,
    private val imprtRepository: ImprtRepository
) : ViewModel() {


    val loading = mutableStateOf(false)
    val error = mutableStateOf("")

    private var _page = mutableStateOf(1)
    val page: State<Int> = _page

    private var scrollPosition = 0

    private var _imprtState = mutableStateListOf<ImpromptuCardInfo>()
    val imprtState: SnapshotStateList<ImpromptuCardInfo> = _imprtState

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)
    val userDistrictId : State<Int?> =  _userDistrictId

    private var _userDistrictName = mutableStateOf<String>("지역구")
    val userDistrictName: State<String> = _userDistrictName


    //day
    private var _imprtDayList = mutableStateListOf<Int>()
    val imprtDayList: SnapshotStateList<Int> = _imprtDayList

    //size
    private var _imprtSizeState = mutableStateOf<Int?>(null)
    val imprtSizeState: State<Int?> = _imprtSizeState

    //schedule
    private var _imprtScheduleState = mutableStateOf<Int?>(null)
    val imprtScheduleState: State<Int?> = _imprtScheduleState


    /*
    * pagination
    * */

    private fun appendList(list: List<ImpromptuCardInfo>) {
        _imprtState.addAll(list)
    }

    fun getNewPage() = viewModelScope.launch {
        if ((scrollPosition + 1) >= (page.value * Constants.PAGE_SIZE)) {
            incrementPage()
            Log.d("{HomeViewModel.newPage}", "${page.value}")

            if (page.value > 1) {
                getImprt()
            }
        }
    }

    private fun incrementPage() {
        _page.value = _page.value + 1
    }

    fun onChangeScrollPosition(position: Int) {
        scrollPosition = position
    }


    fun setImprtDayList(list: SnapshotStateList<Int>) {
        _imprtDayList = list
    }

    fun setImprtSize(size: Int?) {
        _imprtSizeState.value = size
        Log.d("{ImpromptuViewModel.setImprtSize}", _imprtSizeState.value.toString())

    }

    fun setImprtSchedule(size: Int?) {
        _imprtScheduleState.value = size
    }

    private fun resetPage() {
        _page.value = 1
    }

    private var _resetState = mutableStateOf(false)
    val resetState: State<Boolean> = _resetState

    fun resetFilter() {
        _resetState.value = true
        _imprtDayList.clear()
        _userDistrictId.value = null
        _userDistrictName.value = "지역구"
        _imprtScheduleState.value = null
        _imprtSizeState.value = null
        resetPage()
    }

    private var _isAppliedFilter = false

    fun applyFilter() {
        _isAppliedFilter = true
        resetPage()
        getImprt()
    }


    private fun getImprt() = viewModelScope.launch {

        val response = imprtRepository.getImpromptu(
            areaId = if (_userDistrictId.value == null) null else _userDistrictId.value!! + 1,
            days = if (_imprtDayList.isEmpty()) null else _imprtDayList.map { it + 1 },
            memberCountIndex = if (_imprtSizeState.value == null) null else _imprtSizeState.value!! + 1,
            page = _page.value,
            scheduleIndex = if (_imprtScheduleState.value == null) null else _imprtScheduleState.value!! + 1
        )

        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    if (res.result.first) _imprtState.clear()
                    appendList(res.result.content.map {
                        ImpromptuCardInfo(
                            id = it.impromptuId,
                            remainingDays = it.dday,
                            title = it.title,
                            isPinned = it.isPinUp,
                            time = "${dateDashToCol(it.date)} ${it.day} ${doubleToTime(it.startTime)}",
                            detailAddress = it.location ?: "장소 미정",
                            imprtImage = it.impromptuImage,
                            gatheredPeople = it.nowMemberCount,
                            totalCount = it.recruitmentCount
                        )
                    })
                } else {
                    error.value = res.message
                }
            }
        } else error.value = "번개 조회에 실패했습니다."
        loading.value = false

    }

    private val _getImprtFilterState = mutableStateOf(BaseState())
    var getImprtFilterState: State<BaseState> = _getImprtFilterState


    fun getImprtFilter() = viewModelScope.launch {
        if (!_isAppliedFilter) {

            loading.value = true
            val response = imprtRepository.getImprtFilter()
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) {
                        _imprtScheduleState.value =
                            if (res.result.impromptuPickScheduleIndex == null) null else res.result.impromptuPickScheduleIndex - 1
                        res.result.impromptuPickDays?.forEach {
                            if (!_imprtDayList.contains(it - 1)) _imprtDayList.add(
                                it - 1
                            )
                        }
                        _imprtSizeState.value =
                            if (res.result.impromptuPickMemberCountValue == null) null else res.result.impromptuPickMemberCountValue - 1
                        _userDistrictName.value = res.result.impromptuPickAreaName ?: "지역구"
                        _userDistrictId.value =
                            if (res.result.impromptuPickAreaId == null) null else res.result.impromptuPickAreaId - 1
                        resetPage()
                        getImprt()

                    } else {
                        loading.value = false
                        _getImprtFilterState.value = BaseState(error = res.message)
                    }
                }
            } else {
                loading.value = false
                _getImprtFilterState.value = BaseState(error = "필터 조회에 실패했습니다.")
            }
        }
    }

    /*
    * districts
    * */

    fun setUserDistrict(districtId: Int, districtName: String) {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        resetPage()
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