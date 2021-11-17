package wupitch.android.data.remote.dto


data class LoginRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: LoginResult
)