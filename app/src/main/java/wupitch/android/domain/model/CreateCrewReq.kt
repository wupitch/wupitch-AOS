package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class CreateCrewReq(
    val ageList: List<Int>,
    val areaId: Int,
    val conference: Int,
    val days: List<Int>,
    val extraInfoList: List<Int>,
    val fridayEndTime: Int,
    val fridayStartTime: Double,
    val guestConference: Int,
    val inquiries: String,
    val introduction: String,
    val location: String,
    val memberCount: Int,
    val mondayEndTime: Int,
    val mondayStartTime: Double,
    val saturdayEndTime: Int,
    val saturdayStartTime: Double,
    val sportsId: Int,
    val sundayEndTime: Int,
    val sundayStartTime: Double,
    val thursdayEndTime: Int,
    val thursdayStartTime: Double,
    val title: String,
    val tuesdayEndTime: Int,
    val tuesdayStartTime: Double,
    val wednesdayEndTime: Int,
    val wednesdayStartTime: Double
)