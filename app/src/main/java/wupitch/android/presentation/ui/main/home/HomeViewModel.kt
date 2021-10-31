package wupitch.android.presentation.ui.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import wupitch.android.data.remote.CrewCardInfo
import javax.inject.Inject


class HomeViewModel : ViewModel() {

    val loading = mutableStateOf(false)
    val crewList : MutableState<List<CrewCardInfo>> = mutableStateOf(
        listOf<CrewCardInfo>(
            CrewCardInfo(
                0,
                "축구",
                "법정동",
                true,
                "크루이름",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37"
            ),
            CrewCardInfo(
                1,
                "농구",
                "이매동",
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
                "이매동",
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
                "이매동",
                false,
                "농구하자히히",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                54,
                "농구",
                "이매동",
                false,
                "농구하자고고씽하하",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                56,
                "농구",
                "이매동",
                false,
                "농구하자고고씽호호",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                77,
                "농구",
                "이매동",
                false,
                "농구하자고고씽ㅇㅇㅇ",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            )
        )
    )



}