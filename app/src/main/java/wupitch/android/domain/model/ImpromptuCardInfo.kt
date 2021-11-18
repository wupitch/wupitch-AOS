package wupitch.android.domain.model

data class ImpromptuCardInfo(
    val remainingDays :Int,
    val id : Int,
    val isPinned : Boolean,
    val title : String,
    val isBiweekly : Boolean,
    val time : String,
    val isMoreThanOnceAWeek : Boolean,
    val detailAddress : String,
    val gatheredPeople : Int,
    val totalCount : Int
)
