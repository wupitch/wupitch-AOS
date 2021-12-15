package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetCrewPostResultDto


data class GetCrewPostRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCrewPostResultDto>
)