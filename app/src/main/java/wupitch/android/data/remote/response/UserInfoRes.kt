package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.UserInfoResultDto


data class UserInfoRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserInfoResultDto
)