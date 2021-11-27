package wupitch.android.domain.model

data class CrewCardInfo(
    val id : Int,
    val sportId : Int,
    val crewImage : String?,
    val isPinned : Boolean,
    val title : String,
    val time : String,
    val isMoreThanOnceAWeek : Boolean,
    val detailAddress : String
)
