package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.UserPhoneNumResult


data class UserPhoneNumRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserPhoneNumResult
)