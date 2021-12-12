package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetImprtFilterResult


data class GetImprtFilterRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetImprtFilterResult
)