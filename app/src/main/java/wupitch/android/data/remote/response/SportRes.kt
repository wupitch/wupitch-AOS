package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.SportResultDto


data class SportRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<SportResultDto>
)