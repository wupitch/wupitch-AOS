package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class CreateImprtReq(
    val areaId: Int,
    val date: String,
    val dues: Int?,
    val endTime: Double,
    val inquiries: String,
    val introduction: String,
    val location: String?,
    val materials: String?,
    val recruitmentCount: Int,
    val startTime: Double,
    val title: String
)