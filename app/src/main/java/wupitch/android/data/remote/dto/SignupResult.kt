package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SignupResult(
    val accountId: Int,
    val email: String,
    val introduce: String,
    val isPushAgree: Boolean,
    val jwt: String,
    val nickname: String
)