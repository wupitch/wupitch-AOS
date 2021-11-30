package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class Schedule(
    val day: String,
    val dayIdx: Int,
    val endTime: Double?,
    val startTime: Double
)