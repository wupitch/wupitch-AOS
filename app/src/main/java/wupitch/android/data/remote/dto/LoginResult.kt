package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LoginResult(
    val accountId: Int,
    val email: String,
    val jwt: String
)