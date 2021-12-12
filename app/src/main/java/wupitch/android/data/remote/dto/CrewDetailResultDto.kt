package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.util.convertedAge
import wupitch.android.util.convertedCrewFee
import wupitch.android.util.convertedSchedule

data class CrewDetailResultDto(
    val ageTable: List<String>,
    val areaName: String?,
    val clubId: Int,
    val clubTitle: String,
    val crewImage: String?,
    val crewName: String?,
    val creatorAccountId: Int,
    val dues: Int?,
    val extraList: List<String>,
    val guestDues: Int?,
    val introduction: String,
    val isPinUp: Boolean,
    val isSelect: Boolean,
    val memberCount: Int,
    val schedules: List<Schedule>,
    val sportsId: Int,
    val sportsName: String,
    val materials: String?,
    val inquiries: String
)

fun CrewDetailResultDto.toCrewDetailResult() : CrewDetailResult {
    return CrewDetailResult(
        ageTable = convertedAge(ageTable),
        areaName = areaName ?: "장소 미정",
        clubId = clubId,
        clubTitle = clubTitle,
        crewImage = crewImage,
        crewName = crewName ?: "",
        dues = convertedCrewFee(dues, guestDues),
        extraList = extraList,
        introduction = introduction,
        memberCount = "${memberCount}명",
        schedules = convertedSchedule(schedules),
        sportsId = sportsId - 1,
        materials = materials,
        inquiries = inquiries,
        isPinUp = isPinUp,
        isSelect = isSelect
    )
}