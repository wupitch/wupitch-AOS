package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)