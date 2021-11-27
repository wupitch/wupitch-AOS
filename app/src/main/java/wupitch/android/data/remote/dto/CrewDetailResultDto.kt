package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CrewDetailResultDto(
    val ageTable: List<String>,
    val areaName: String?,
    val clubId: Int,
    val clubTitle: String,
    val crewImage: String?,
    val crewName: String?,
    val dues: Int?,
    val extraList: List<String>,
    val guestDues: Int?,
    val introduction: String,
    val memberCount: Int,
    val schedules: List<Schedule>,
    val sportsId: Int,
    val sportsName: String,
    val materials : String?,
    val inquiries : String
)