package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.GetMemberDetailResultDto


data class GetMemberDetailRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetMemberDetailResultDto
)