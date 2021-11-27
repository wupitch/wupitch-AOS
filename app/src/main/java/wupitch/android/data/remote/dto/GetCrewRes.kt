package wupitch.android.data.remote.dto


data class GetCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetCrewResult
)