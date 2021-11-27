package wupitch.android.presentation.ui.main.home.crew_detail

import wupitch.android.domain.model.CrewDetailResult

data class CrewDetailState(
    val isLoading : Boolean = false,
    val data : CrewDetailResult? = null,
    val error : String = ""
)
