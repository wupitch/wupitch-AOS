package wupitch.android.data.remote.dto



data class DistrictRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ArrayList<DistrictResultDto>
)