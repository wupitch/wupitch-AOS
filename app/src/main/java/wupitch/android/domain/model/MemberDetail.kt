package wupitch.android.domain.model

data class MemberDetail(
    val id : Int,
    val userImage: String?,
    val userName: String,
    val userAgeGroup: String,
    val userArea: String,
    val userPhoneNum: String,
    val userSports: List<Int>,
    val intro: String,
    val visitorDate : String?
)
