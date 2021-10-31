package wupitch.android.common

data class ResultState<T>(
    var isLoading : Boolean = false,
    var result : T? = null,
    var error : String = ""
)
