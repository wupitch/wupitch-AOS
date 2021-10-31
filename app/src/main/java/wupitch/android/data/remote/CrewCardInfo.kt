package wupitch.android.data.remote

data class CrewCardInfo(
    val sport : String,
    val location : String,
    val isPinned : Boolean,
    val name : String,
    val isBiweekly : Boolean,
    val time : String,
    val isMoreThanOnceAWeek : Boolean,
    val detailAddress : String
)
