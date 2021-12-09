package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

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