package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.UserSportsResult


data class UserSportsRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserSportsResult
)