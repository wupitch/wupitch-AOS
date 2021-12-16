package wupitch.android.domain.model

data class CrewMember(
    val id : Int,
    val userImage : String?,
    val userName : String,
    val isLeader : Boolean,
    val isValid : Boolean?
)
