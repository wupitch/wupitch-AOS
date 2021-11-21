package wupitch.android.presentation.ui.main.impromptu

import wupitch.android.domain.model.ImpromptuCardInfo

data class ImpromptuListState(
    val isLoading : Boolean = false,
    val data : List<ImpromptuCardInfo> = emptyList(),
    val error : String = ""
)
