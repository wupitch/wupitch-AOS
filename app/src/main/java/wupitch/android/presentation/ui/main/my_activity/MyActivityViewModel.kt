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

class MyActivityViewModel : ViewModel(){

    //todo isPinned 무조건 false
    private var _myImprtState : MutableState<MyImprtState> = mutableStateOf(MyImprtState())
    val myImprtState : State<MyImprtState> = _myImprtState

    private var _myCrewState : MutableState<MyCrewState> = mutableStateOf(MyCrewState())
    val myCrewState : State<MyCrewState> = _myCrewState

    var crewReportState = mutableStateOf(false)

    private var _imprtReportState = mutableStateOf(false)
    val imprtReportState : State<Boolean> = _imprtReportState

    fun getMyCrew() = viewModelScope.launch {
        _myCrewState.value = MyCrewState(isLoading = true)
        delay(500L)
//        _myCrewState.value = MyCrewState()
        _myCrewState.value = MyCrewState(data = listOf<CrewCardInfo>(
            CrewCardInfo(
                0,
                "축구",
                false,
                "가나다라마바사아자차...",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37"
            ),
            CrewCardInfo(
                1,
                "농구",
                false,
                "농구하자고고씽",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                23,
                "농구",
                false,
                "아무이름이지롱",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                44,
                "농구",
                false,
                "농구하자히히",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            )
        ))

    }

    fun getMyImpromptu() = viewModelScope.launch {
        _myImprtState.value = MyImprtState(isLoading = true)
        delay(500L)
        _myImprtState.value = MyImprtState(data = listOf<ImpromptuCardInfo>(
            ImpromptuCardInfo(
                1,
                11,
                false,
                "가나다라마바사아자차...",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                10,
                12,
                false,
                "번개합시다",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                1,
                3
            ),
            ImpromptuCardInfo(
                1,
                13,
                false,
                "날씨좋다 모이자",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                14,
                false,
                "천둥번개!",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                15,
                false,
                "번개 제목",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                16,
                false,
                "마지막 번개",
                "월요일 23:00 - 24:00",
                "동백 2로 37",
                2,
                3
            ),
        ))
    }

    fun setCrewReportState() {
        crewReportState.value = true
    }

    fun postCrewReport(content : String) {

    }
}