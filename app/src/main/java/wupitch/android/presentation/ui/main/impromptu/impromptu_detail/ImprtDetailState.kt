package wupitch.android.presentation.ui.main.impromptu.impromptu_detail

import wupitch.android.data.remote.dto.ImprtDetailResult

data class ImprtDetailState(
    val isLoading : Boolean = false,
    val data : ImprtDetailResult? = null,
    val error : String = ""
)
