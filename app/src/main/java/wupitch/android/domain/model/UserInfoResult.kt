package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class UserInfoResult(
    val introduce: String,
    val isPushAgree: Boolean,
    val nickname: String,
    val profileImageUrl: String
)