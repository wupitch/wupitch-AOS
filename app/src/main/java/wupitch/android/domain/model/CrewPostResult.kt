package wupitch.android.domain.model

data class CrewPostResult(
    val id : Int,
    val isAnnounce : Boolean,
    val announceTitle : String?,
    val userImage : String?,
    val userName : String,
    val isLeader : Boolean,
    val content : String,
    var isLiked : Boolean,
    var likedNum : Int,
    val date : String
)
