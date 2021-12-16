package wupitch.android.presentation.ui.main.my_activity.my_crew

import wupitch.android.domain.model.CrewMember

data class CrewMemberState(
    val isLoading : Boolean = false,
    val data : List<CrewMember> = emptyList(),
    val error : String = ""
)