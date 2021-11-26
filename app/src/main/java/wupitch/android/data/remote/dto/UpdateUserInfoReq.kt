package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class UpdateUserInfoReq(
    val ageNum: Int? = null,
    val areaId: Int?= null,
    val introduce: String?= null,
    val isPushAgree: Boolean?= null,
    val nickname: String?= null,
    val otherSports: String?= null,
    val phoneNumber: String?= null,
    val sportsList: List<Int>?= null
)