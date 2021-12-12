package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.LoginResult


data class LoginRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: LoginResult
)