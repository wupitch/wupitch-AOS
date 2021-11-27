package wupitch.android.data.remote.dto


data class UserPhoneNumRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: UserPhoneNumResult
)