package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import wupitch.android.domain.model.ImprtMember

data class ImprtMemberState(
    val isLoading : Boolean = false,
    val data : List<ImprtMember> = emptyList(),
    val error : String = ""
)