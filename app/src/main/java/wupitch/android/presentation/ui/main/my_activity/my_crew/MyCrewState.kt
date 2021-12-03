package wupitch.android.presentation.ui.main.my_activity.my_crew

import wupitch.android.domain.model.CrewCardInfo

data class MyCrewState(
    val isLoading : Boolean = false,
    val data : List<CrewCardInfo> = emptyList(),
    val error : String = ""
)
