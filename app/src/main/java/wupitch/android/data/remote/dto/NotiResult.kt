package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NotiResult(
    val accountId: Int,
    val contents: String,
    val fcmId: Int,
    val isChecked: Boolean,
    val title: String,
    val dateTime : String
)