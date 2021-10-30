package wupitch.android.data.repository


import com.google.gson.annotations.SerializedName

data class KakaoLoginReq(
    val email: String,
    val genderType: String,
    val id: Long,
    val nickname: String
)