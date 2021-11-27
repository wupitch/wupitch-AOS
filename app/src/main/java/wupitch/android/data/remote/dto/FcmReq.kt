package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class FcmReq(
    val contents: String,
    val targetToken: String,
    val title: String
)