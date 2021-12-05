package wupitch.android.presentation.ui.main.my_activity

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.presentation.ui.main.my_activity.my_crew.MyCrewState
import wupitch.android.presentation.ui.main.my_activity.my_impromptu.ImprtState

class MyActivityViewModel : ViewModel() {

    //todo isPinned 무조건 false
    private var _myImprtState: MutableState<ImprtState> = mutableStateOf(ImprtState())
    val myImprtState: State<ImprtState> = _myImprtState

    private var _myCrewState: MutableState<MyCrewState> = mutableStateOf(MyCrewState())
    val myCrewState: State<MyCrewState> = _myCrewState


    fun getMyCrew() = viewModelScope.launch {
//        _myCrewState.value = MyCrewState()
//        _myCrewState.value = MyCrewState(isLoading = true)
//        delay(500L)
        _myCrewState.value = MyCrewState(
            data = listOf(
                CrewCardInfo(
                id = 76,
                    sportId = 5,
                    crewImage = null,
                    isPinned = false,
                    title= "뛰기만 합니다.",
                    time = "일요일 12:00 - 14:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "울집 앞"
            ),
                CrewCardInfo(
                    id = 77,
                    sportId = 1,
                    crewImage = null,
                    isPinned = false,
                    title= " 축구만 합니다.",
                    time = "일요일 12:00 - 14:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "울집 앞"
                ),
                CrewCardInfo(
                    id = 88,
                    sportId = 3,
                    crewImage = null,
                    isPinned = false,
                    title= "배구만 합니다.",
                    time = "일요일 12:00 - 14:00",
                    isMoreThanOnceAWeek = true,
                    detailAddress = "울집 앞"
                )
            )
        )


    }

    fun getMyImpromptu() = viewModelScope.launch {
//        _myImprtState.value = ImprtState(isLoading = true)
//        delay(500L)
        _myImprtState.value = ImprtState()

    }


}