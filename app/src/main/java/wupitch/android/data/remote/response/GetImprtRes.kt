package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetImprtResult


data class GetImprtRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetImprtResult
)