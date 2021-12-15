package wupitch.android.presentation.ui.main.my_activity.my_crew

import wupitch.android.domain.model.CrewPostResult

data class CrewPostState(
    val isLoading : Boolean = false,
    val data : List<CrewPostResult> = listOf(),
    val error : String = ""
)
