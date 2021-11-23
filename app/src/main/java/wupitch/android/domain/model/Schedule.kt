package wupitch.android.domain.model


import com.google.gson.annotations.SerializedName

data class Schedule(
    val dayIdx: Int,
    val endTime: Double,
    val startTime: Double
)