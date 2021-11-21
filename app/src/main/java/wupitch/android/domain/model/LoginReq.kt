package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class LoginReq(
    val email: String,
    val password: String
)