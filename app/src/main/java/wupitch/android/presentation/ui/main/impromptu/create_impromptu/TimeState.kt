package wupitch.android.presentation.ui.main.impromptu.create_impromptu

import androidx.compose.runtime.MutableState

data class TimeState(
    var startTime : MutableState<String>,
    var endTime : MutableState<String>,
    var isStartTimeSet : MutableState<Boolean?>,
    var isEndTimeSet : MutableState<Boolean?>
)
