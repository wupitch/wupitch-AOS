package wupitch.android.data.remote.dto


data class GetImprtRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetImprtResult
)