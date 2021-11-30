package wupitch.android.presentation.ui.main.home.crew_detail

data class JoinState(
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val error : String  = "",
    val code : Int = -1,
    val result : Boolean? = null
)
