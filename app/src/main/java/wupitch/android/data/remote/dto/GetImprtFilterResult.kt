package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class GetImprtFilterResult(
    val impromptuPickAreaId: Int?,
    val impromptuPickAreaName: String?,
    val impromptuPickDays: List<Int>?,
    val impromptuPickMemberCountValue: Int?,
    val impromptuPickScheduleIndex: Int?
)