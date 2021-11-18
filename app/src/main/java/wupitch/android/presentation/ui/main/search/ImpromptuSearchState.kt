package wupitch.android.presentation.ui.main.search

import wupitch.android.domain.model.ImpromptuCardInfo

data class ImpromptuSearchState(
    val isLoading : Boolean = false,
    val data : List<ImpromptuCardInfo> = emptyList(),
    val error : String  = ""
)
