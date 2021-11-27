package wupitch.android.data.remote.dto


data class ImprtDetailRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ImprtDetailResultDto
)