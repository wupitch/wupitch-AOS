package wupitch.android.common

data class BaseState(
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val error : String  = ""
)
