package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

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