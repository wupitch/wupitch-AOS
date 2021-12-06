package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class GetMyCrewRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCrewContent>
)