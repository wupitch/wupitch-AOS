package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import wupitch.android.domain.model.ImpromptuCardInfo

data class ImprtState(
    val isLoading : Boolean = false,
    val data : List<ImpromptuCardInfo> = emptyList(),
    val error : String = ""
)
