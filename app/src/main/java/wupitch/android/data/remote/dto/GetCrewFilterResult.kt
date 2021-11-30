package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class GetCrewFilterResult(
    val crewPickAgeList: List<Int>?,
    val crewPickAreaId: Int?,
    val crewPickAreaName : String?,
    val crewPickDays: List<Int>?,
    val crewPickMemberCountValue: Int?,
    val crewPickSportsList: List<Int>?
)