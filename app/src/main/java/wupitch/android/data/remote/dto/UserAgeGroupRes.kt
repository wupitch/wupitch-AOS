package wupitch.android.data.remote.dto


data class UserAgeGroupRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserAgeGroupResult
)