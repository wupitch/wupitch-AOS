package wupitch.android.presentation.ui.main.impromptu_detail

data class JoinImpromptuState(
    val isLoading : Boolean = false,
    val isSuccess : Boolean? = null,
    val error : String = ""
)