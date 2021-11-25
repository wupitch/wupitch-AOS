package wupitch.android.presentation.ui.main.my_activity

import wupitch.android.domain.model.ImpromptuCardInfo

data class MyImprtState(
    val isLoading : Boolean = false,
    val data : List<ImpromptuCardInfo> = emptyList(),
    val error : String = ""
)
