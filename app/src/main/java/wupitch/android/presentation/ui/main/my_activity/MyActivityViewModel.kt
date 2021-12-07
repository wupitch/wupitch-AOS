package wupitch.android.presentation.ui.main.my_activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.my_activity.my_crew.MyCrewState
import wupitch.android.presentation.ui.main.my_activity.my_impromptu.ImprtState
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class MyActivityViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val imprtRepository: ImprtRepository
) : ViewModel() {

    private var _myImprtState: MutableState<ImprtState> = mutableStateOf(ImprtState())
    val myImprtState: State<ImprtState> = _myImprtState

    private var _myCrewState: MutableState<MyCrewState> = mutableStateOf(MyCrewState())
    val myCrewState: State<MyCrewState> = _myCrewState


    fun getMyCrew() = viewModelScope.launch {
        _myCrewState.value = MyCrewState(isLoading = true)

        val response = crewRepository.getMyCrews()
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) _myCrewState.value = MyCrewState(data = res.result.map {
                    CrewCardInfo(
                        id = it.clubId,
                        sportId = it.sportsId - 1,
                        crewImage = it.crewImage,
                        isPinned = false,
                        title = it.clubTitle,
                        time = "${it.schedules[0].day} ${doubleToTime(it.schedules[0].startTime)}-${doubleToTime(it.schedules[0].endTime)}",
                        isMoreThanOnceAWeek = it.schedules.size > 1,
                        detailAddress = it.areaName ?: "장소 미정"
                    ) }
                ) else _myCrewState.value = MyCrewState(error = res.message)
            }
        }else _myCrewState.value = MyCrewState(error = "내 크루 조회에 실패했습니다.")
    }

    fun getMyImpromptu() = viewModelScope.launch {
        _myImprtState.value = ImprtState(isLoading = true)

        val response = imprtRepository.getMyImprt()
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    _myImprtState.value = ImprtState(data = res.result.map {
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
                    _myImprtState.value = ImprtState(error = res.message)
                }
            }
        } else  _myImprtState.value = ImprtState(error = "내 번개 조회에 실패했습니다.")

    }


}