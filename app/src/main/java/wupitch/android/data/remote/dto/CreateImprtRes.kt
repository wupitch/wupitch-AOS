package wupitch.android.data.remote.dto


data class CreateImprtRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CreateImprtResult
)