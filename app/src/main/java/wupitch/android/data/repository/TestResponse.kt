package wupitch.android.data.repository


import com.google.gson.annotations.SerializedName

data class TestResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)