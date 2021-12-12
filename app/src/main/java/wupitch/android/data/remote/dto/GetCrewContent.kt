package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.util.doubleToTime

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

fun GetCrewContent.toCrewCardInfo() : CrewCardInfo{
    return  CrewCardInfo(
        id = clubId,
        sportId = sportsId -1,
        isPinned = isPinUp,
        time = "${schedules[0].day} ${doubleToTime(schedules[0].startTime)}-${doubleToTime(schedules[0].endTime)}",
        isMoreThanOnceAWeek = schedules.size >1,
        detailAddress = areaName ?: "장소 미정",
        crewImage = crewImage,
        title = clubTitle
    )
}