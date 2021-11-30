package wupitch.android.data.remote.dto


data class GetCrewFilterRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetCrewFilterResult
)