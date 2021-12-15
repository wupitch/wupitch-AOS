package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CrewPostReq(
    val contents: String,
    val isNotice: Boolean,
    val noticeTitle: String?
)