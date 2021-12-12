package wupitch.android.data.remote.response

import wupitch.android.data.remote.dto.DistrictResultDto


data class DistrictRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<DistrictResultDto>
)