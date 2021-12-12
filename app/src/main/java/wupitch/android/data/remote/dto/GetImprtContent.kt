package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime

data class GetImprtContent(
    val date: String,
    val day: String,
    val dday: Int,
    val endTime: Double,
    val impromptuId: Int,
    val impromptuImage: String?,
    val isPinUp: Boolean,
    val location: String?,
    val nowMemberCount: Int,
    val recruitmentCount: Int,
    val startTime: Double,
    val title: String
)

fun GetImprtContent.toImprtCardInfo() : ImpromptuCardInfo {
    return ImpromptuCardInfo(
        id = impromptuId,
        remainingDays = dday,
        title = title,
        isPinned = isPinUp,
        time = "${dateDashToCol(date)} $day ${doubleToTime(startTime)}",
        detailAddress = location ?: "장소 미정",
        imprtImage = impromptuImage,
        gatheredPeople = nowMemberCount,
        totalCount = recruitmentCount
    )
}