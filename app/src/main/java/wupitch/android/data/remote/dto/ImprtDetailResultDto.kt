package wupitch.android.data.remote.dto


import wupitch.android.domain.model.ImprtDetailResult
import wupitch.android.util.convertedFee
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime

data class ImprtDetailResultDto(
    val date: String,
    val day: String,
    val dday: Int,
    val dues: Int?,
    val creatorAccountId: Int,
    val endTime: Double,
    val impromptuId: Int,
    val impromptuImage: String?,
    val inquiries: String,
    val introduction: String,
    val location: String?,
    val materials: String?,
    val isPinUp : Boolean,
    val isSelect : Boolean,
    val nowMemberCount: Int,
    val recruitmentCount: Int,
    val startTime: Double,
    val title: String
)

fun ImprtDetailResultDto.toImprtDetailResult() : ImprtDetailResult {
    return  ImprtDetailResult(
        date = "${dateDashToCol(date)} $day",
        time = "${doubleToTime(startTime)} - ${doubleToTime(endTime)}",
        dday = dday,
        dues = if (dues == null) null else convertedFee(dues),
        impromptuId = impromptuId,
        impromptuImage = impromptuImage,
        inquiries = inquiries,
        introduction = introduction,
        location = location ?: "장소 미정",
        materials = materials,
        recruitStatus = "${nowMemberCount}/${recruitmentCount}명 참여",
        title = title,
        isPinUp = isPinUp,
        isSelect = isSelect
    )
}