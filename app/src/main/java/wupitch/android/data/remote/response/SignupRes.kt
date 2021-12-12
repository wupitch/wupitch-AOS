package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.SignupResult


data class SignupRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: SignupResult
)