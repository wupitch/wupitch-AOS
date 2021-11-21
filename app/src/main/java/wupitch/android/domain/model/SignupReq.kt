package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class SignupReq(
    val email: String,
    val introduce: String,
    val isPushAgree: Boolean,
    val nickname: String,
    val password: String
)