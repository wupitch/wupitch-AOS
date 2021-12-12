package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.UserDistrictResult


data class UserDistrictRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserDistrictResult
)