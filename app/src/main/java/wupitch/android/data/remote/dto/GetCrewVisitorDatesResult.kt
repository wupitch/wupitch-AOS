package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class GetCrewVisitorDatesResult(
    val clubId: Int,
    val days: List<String>,
    val guestDue: Int,
    val localDates: List<String>
)