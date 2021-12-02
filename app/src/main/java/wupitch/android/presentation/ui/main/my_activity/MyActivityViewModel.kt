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
        _myCrewState.value = MyCrewState()
//        _myCrewState.value = MyCrewState(isLoading = true)
//        delay(500L)
//        _myCrewState.value = MyCrewState(
//            data = listOf(
//                CrewCardInfo(
//                id = 28,
//                    sportId = 2,
//                    crewImage = null,
//                    isPinned = false,
//                    title= "test crew",
//                    time = "12:00",
//                    isMoreThanOnceAWeek = true,
//                    detailAddress = "울집 앞"
//            ))
//        )


    }

    fun getMyImpromptu() = viewModelScope.launch {
//        _myImprtState.value = ImprtState(isLoading = true)
//        delay(500L)
        _myImprtState.value = ImprtState()

    }


}