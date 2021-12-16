package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetCrewMembersResultDto


data class GetCrewMembersRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCrewMembersResultDto>
)