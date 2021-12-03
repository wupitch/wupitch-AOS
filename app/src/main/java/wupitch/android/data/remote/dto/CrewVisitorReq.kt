package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CrewVisitorReq(
    val crewId: Int,
    val date: String
)