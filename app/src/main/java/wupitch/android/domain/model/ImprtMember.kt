package wupitch.android.domain.model

data class ImprtMember(
    val id : Int,
    val userImage : String?,
    val userName : String,
    val isLeader : Boolean,
    val isValid : Boolean
)
