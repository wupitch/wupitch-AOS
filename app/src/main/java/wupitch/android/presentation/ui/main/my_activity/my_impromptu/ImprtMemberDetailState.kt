package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import wupitch.android.domain.model.MemberDetail

data class ImprtMemberDetailState(
    val isLoading: Boolean = false,
    val data: MemberDetail? = null,
    val error: String = ""
)
