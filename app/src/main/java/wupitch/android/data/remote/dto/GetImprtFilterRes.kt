package wupitch.android.data.remote.dto


data class GetImprtFilterRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetImprtFilterResult
)