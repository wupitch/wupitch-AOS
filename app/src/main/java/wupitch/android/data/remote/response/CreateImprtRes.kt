package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.CreateImprtResult


data class CreateImprtRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CreateImprtResult
)