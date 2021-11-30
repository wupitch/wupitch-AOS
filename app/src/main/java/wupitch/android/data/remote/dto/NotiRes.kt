package wupitch.android.data.remote.dto


data class NotiRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<NotiResult>
)