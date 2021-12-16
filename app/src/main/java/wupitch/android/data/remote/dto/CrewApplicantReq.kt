package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CrewApplicantReq(
    val accountId: Int,
    val clubId: Int,
    val isGuest: Boolean
)