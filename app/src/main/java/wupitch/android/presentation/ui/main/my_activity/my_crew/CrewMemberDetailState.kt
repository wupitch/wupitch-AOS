package wupitch.android.presentation.ui.main.my_activity.my_crew

import wupitch.android.domain.model.MemberDetail

data class CrewMemberDetailState(
    val isLoading: Boolean = false,
    val data: MemberDetail? = null,
    val error: String = ""
)
