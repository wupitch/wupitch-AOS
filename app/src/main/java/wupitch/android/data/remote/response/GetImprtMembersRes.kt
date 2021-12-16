package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetImprtMembersResultDto


data class GetImprtMembersRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetImprtMembersResultDto>
)