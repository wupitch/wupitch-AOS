package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class KakaoLoginResult(
    val accountId: Int,
    val jwt: String,
    val oauthId: String
)