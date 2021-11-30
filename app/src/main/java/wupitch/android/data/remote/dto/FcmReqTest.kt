package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class FcmReqTest(
    val contents: String,
    val deviceToken: String,
    val title: String
)