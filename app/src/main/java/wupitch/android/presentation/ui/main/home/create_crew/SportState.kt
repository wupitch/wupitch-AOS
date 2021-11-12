package wupitch.android.presentation.ui.main.home.create_crew

import wupitch.android.domain.model.FilterItem

data class SportState(
    val isLoading : Boolean = false,
    val data : List<FilterItem> = emptyList(),
    val error : String = ""
)
