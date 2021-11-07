package wupitch.android.data.remote.dto


data class SportRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<SportResultDto>
)