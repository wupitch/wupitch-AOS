package wupitch.android.data.remote.dto


data class UserSportsRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserSportsResult
)