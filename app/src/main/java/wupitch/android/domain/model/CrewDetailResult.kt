package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class CrewDetailResult(
    val ageTable: String,
    val areaName: String,
    val clubId: Int,
    val clubTitle: String,
    val crewImage: String?,
    val crewName: String,
    val dues: List<String>,
    val extraList: List<String>,
    val introduction: String,
    val memberCount: String,
    val isPinUp : Boolean,
    val isSelect : Boolean,
    val schedules: List<String>,
    val sportsId: Int,
    val materials : String?,
    val inquiries : String?
)