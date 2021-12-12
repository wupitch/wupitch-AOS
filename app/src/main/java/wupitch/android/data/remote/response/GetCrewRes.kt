package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetCrewResult


data class GetCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetCrewResult
)