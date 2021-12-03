package wupitch.android.presentation.ui.main.my_activity.my_crew

data class CrewPost(
    val id : Int,
    val isAnnounce : Boolean,
    val announceTitle : String?,
    val userImage : String?,
    val userName : String,
    val isLeader : Boolean,
    val content : String,
    val isLiked : Boolean,
    val likedNum : Int,
    val date : String
)
