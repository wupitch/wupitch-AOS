package wupitch.android.data.remote.response


import com.google.gson.annotations.SerializedName

data class ValidationRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String
)