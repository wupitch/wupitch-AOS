package wupitch.android.data.remote.dto


data class GetCrewVisitorDates(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetCrewVisitorDatesResult
)