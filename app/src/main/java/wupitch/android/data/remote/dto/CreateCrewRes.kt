package wupitch.android.data.remote.dto


data class CreateCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CreateCrewResult
)