package wupitch.android.data.remote.dto


data class SignupRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupResult
)