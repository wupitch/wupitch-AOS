package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class ImprtDetailResult(
    val date: String,
    val time: String,
    val dday: Int,
    val dues: String?,
    val impromptuId: Int,
    val impromptuImage: String?,
    val inquiries: String,
    val introduction: String,
    val location: String,
    val materials: String?,
    val recruitStatus : String,
    val title: String
)