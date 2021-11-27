package wupitch.android.presentation.ui.main.home

import wupitch.android.domain.model.CrewCardInfo

data class CrewState(
    val isLoading : Boolean = false,
    val data : List<CrewCardInfo> = emptyList(),
    val error : String = ""
)
