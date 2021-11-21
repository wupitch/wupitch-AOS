package wupitch.android.presentation.ui.main.search

import wupitch.android.domain.model.CrewCardInfo

data class CrewSearchState(
    val isLoading : Boolean = false,
    val data : List<CrewCardInfo> = emptyList(),
    val error : String = ""
)
