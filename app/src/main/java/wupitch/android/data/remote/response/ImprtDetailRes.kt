package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.ImprtDetailResultDto


data class ImprtDetailRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ImprtDetailResultDto
)