package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.CrewDetailResultDto


data class CrewDetailRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CrewDetailResultDto
)