package wupitch.android.data.remote.dto

import wupitch.android.domain.model.DistrictResult


data class DistrictRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<DistrictResult>
)