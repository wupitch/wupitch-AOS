package wupitch.android.data.remote.response


import wupitch.android.data.remote.dto.GetCrewContent

data class GetMyCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCrewContent>
)