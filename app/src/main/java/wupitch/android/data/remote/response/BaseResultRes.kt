package wupitch.android.data.remote.response


import wupitch.android.data.remote.dto.Result

data class BaseResultRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)