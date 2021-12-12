package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.UserAgeGroupResult


data class UserAgeGroupRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserAgeGroupResult
)