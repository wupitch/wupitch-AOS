package wupitch.android.presentation.ui.main.home.create_crew

import androidx.compose.runtime.MutableState

data class ScheduleState(
    var day : MutableState<Int>,
    var startTime : MutableState<String>,
    var endTime : MutableState<String>,
    var isStartTimeSet : MutableState<Boolean?>,
    var isEndTimeSet : MutableState<Boolean?>
)
