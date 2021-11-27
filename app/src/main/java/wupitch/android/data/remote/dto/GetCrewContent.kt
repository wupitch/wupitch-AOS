package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class GetCrewContent(
    val areaName: String?,
    val clubId: Int,
    val clubTitle: String,
    val crewImage: String?,
    val introduction: String,
    val isPinUp: Boolean,
    val schedules: List<Schedule>,
    val sportsId: Int,
    val sportsName: String
)