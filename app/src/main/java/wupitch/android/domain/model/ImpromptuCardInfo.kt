package wupitch.android.domain.model

data class ImpromptuCardInfo(
    val remainingDays :Int,
    val id : Int,
    val isPinned : Boolean,
    val title : String,
    val time : String,
    val detailAddress : String,
    val gatheredPeople : Int,
    val totalCount : Int,
    val imprtImage : String?
)
