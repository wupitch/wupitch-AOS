package wupitch.android.data.remote.dto


data class UserInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserInfoResultDto
)