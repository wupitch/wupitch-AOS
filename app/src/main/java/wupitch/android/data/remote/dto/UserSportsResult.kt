package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class UserSportsResult(
    val accountId: Int,
    val list: List<UserSport>?
)