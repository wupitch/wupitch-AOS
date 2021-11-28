package wupitch.android.presentation.ui.main.my_activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo

class MyActivityViewModel : ViewModel() {

    //todo isPinned 무조건 false
    private var _myImprtState: MutableState<ImprtState> = mutableStateOf(ImprtState())
    val myImprtState: State<ImprtState> = _myImprtState

    private var _myCrewState: MutableState<MyCrewState> = mutableStateOf(MyCrewState())
    val myCrewState: State<MyCrewState> = _myCrewState


    fun getMyCrew() = viewModelScope.launch {
        _myCrewState.value = MyCrewState(isLoading = true)
        delay(500L)
        _myCrewState.value = MyCrewState(
            data = listOf<CrewCardInfo>(
                CrewCardInfo(
                    id = 10,
                    sportId = 1,
                    crewImage = null,
                    isPinned = true,
                    title = "크루입니다.",
                    time = "수요일 2:00 - 4:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "상세주소임당"
                ),
                CrewCardInfo(
                    id = 11,
                    sportId = 1,
                    crewImage = null,
                    isPinned = true,
                    title = "크루입니다ㅎㅎ",
                    time = "수요일 2:00 - 4:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "상세주소임당"
                ),
                CrewCardInfo(
                    id = 12,
                    sportId = 1,
                    crewImage = null,
                    isPinned = true,
                    title = "크루입니다ㅋㅋ",
                    time = "수요일 2:00 - 4:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "상세주소임당"
                )
            )
        )

    }

    fun getMyImpromptu() = viewModelScope.launch {
        _myImprtState.value = ImprtState(isLoading = true)
        delay(500L)
        _myImprtState.value = ImprtState(
            data = listOf<ImpromptuCardInfo>(
                ImpromptuCardInfo(
                    remainingDays = 1,
                    id = 10,
                    isPinned = false,
                    title = "번개임당",
                    time = "21.11.30 호요일 12:30",
                    detailAddress = "우리집앞",
                    gatheredPeople = 2,
                    totalCount = 10,
                    imprtImage = null
                ),
                ImpromptuCardInfo(
                    remainingDays = 1,
                    id = 11,
                    isPinned = false,
                    title = "번개임당ㅎㅎ",
                    time = "21.11.30 호요일 12:30",
                    detailAddress = "우리집앞",
                    gatheredPeople = 2,
                    totalCount = 10,
                    imprtImage = null
                ),
                ImpromptuCardInfo(
                    remainingDays = 1,
                    id = 12,
                    isPinned = false,
                    title = "번개임당ㅋㅋ",
                    time = "21.11.30 호요일 12:30",
                    detailAddress = "우리집앞",
                    gatheredPeople = 2,
                    totalCount = 10,
                    imprtImage = null
                )

            )
        )
    }


}