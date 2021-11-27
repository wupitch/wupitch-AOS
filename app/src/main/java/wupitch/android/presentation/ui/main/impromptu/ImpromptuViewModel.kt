package wupitch.android.presentation.ui.main.impromptu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val getDistrictRepository : GetDistrictRepository,
    private val imprtRepository: ImprtRepository
) : ViewModel() {


    private var _imprtState = mutableStateOf(ImprtState())
    val imprtState : State<ImprtState> = _imprtState

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList : State<DistrictState> = _districtList

    private var _userDistrictId = mutableStateOf<Int?>(null)

    private var _userDistrictName = mutableStateOf<String>("지역구")
    val userDistrictName : State<String> = _userDistrictName


    //day
    private var _imprtDayList = mutableStateListOf<Int>()
    val imprtDayList: SnapshotStateList<Int> = _imprtDayList

    //size
    private var _imprtSizeState = mutableStateOf<Int?>(null)
    val imprtSizeState : State<Int?> = _imprtSizeState

    //schedule
    private var _imprtScheduleState = mutableStateOf<Int?>(null)
    val imprtScheduleState : State<Int?> = _imprtScheduleState

    fun setImprtDayList(list: SnapshotStateList<Int>) {
        _imprtDayList = list
    }

    fun setImprtSize(size: Int?) {
        _imprtSizeState.value = size
    }

    fun setImprtSchedule(size: Int?) {
        _imprtScheduleState.value = size
    }

    fun applyFilter() {
        //todo save in data store
        getImprt()
    }

    fun resetFilter() {
        //todo
    }


    fun getImprt () = viewModelScope.launch {
        _imprtState.value = ImprtState(isLoading = true)

        val response = imprtRepository.getImpromptu(
            areaId = if(_userDistrictId.value == null) null else _userDistrictId.value!! +1,
            days = if(_imprtDayList.isEmpty()) null else _imprtDayList.map { it+1 },
            memberCountIdx = if(_imprtSizeState.value == null) null else _imprtSizeState.value!! +1,
            page = 1,
            scheduleIndex = if(_imprtScheduleState.value == null) null else _imprtScheduleState.value!! +1
        )

        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) _imprtState.value = ImprtState(data = res.result.content.map {
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
                })else _imprtState.value = ImprtState(error = res.message)
            }
        }else _imprtState.value = ImprtState(error = "번개 조회에 실패했습니다.")
    }

    fun setUserDistrict(districtId : Int, districtName : String) {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        getImprt()
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