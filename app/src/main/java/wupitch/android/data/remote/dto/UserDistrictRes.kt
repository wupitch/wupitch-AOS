package wupitch.android.data.remote.dto


data class UserDistrictRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserDistrictResult
)