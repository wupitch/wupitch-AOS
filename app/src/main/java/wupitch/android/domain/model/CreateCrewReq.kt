package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class CreateCrewReq(
    val ageList: List<Int>,
    val areaId: Int,
    val conference: Int?,
    val extraInfoList: List<Int>,
    val guestConference: Int?,
    val inquiries: String,
    val introduction: String,
    val location: String?,
    val memberCount: Int,
    val scheduleList: List<Schedule>,
    val sportsId: Int,
    val title: String,
    val materials : String?
)