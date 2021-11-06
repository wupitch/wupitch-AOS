package wupitch.android.domain.model

data class CrewCardInfo(
    val id : Int,
    val sport : String,
    val location : String,
    val isPinned : Boolean,
    val name : String,
    val isBiweekly : Boolean,
    val time : String,
    val isMoreThanOnceAWeek : Boolean,
    val detailAddress : String
)
