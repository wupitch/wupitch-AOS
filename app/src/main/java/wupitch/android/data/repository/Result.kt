package wupitch.android.data.repository


import com.google.gson.annotations.SerializedName

data class Result(
    val accountId: Int,
    val jwt: String,
    val oauthId: String
)