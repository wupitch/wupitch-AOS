package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.CreateCrewResult


data class CreateCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CreateCrewResult
)