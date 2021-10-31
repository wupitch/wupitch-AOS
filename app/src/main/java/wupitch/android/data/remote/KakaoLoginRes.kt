package wupitch.android.data.remote


data class KakaoLoginRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: KakaoLoginResult
)