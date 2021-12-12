package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.NotiResult


data class NotiRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<NotiResult>
)