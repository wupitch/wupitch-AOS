package wupitch.android.data.remote.dto


import wupitch.android.domain.model.MemberDetail
import wupitch.android.util.dateDashToCol

data class GetMemberDetailResultDto(
    val accountId: Int,
    val accountNickname: String,
    val addedAt: String,
    val ageNum: Int,
    val area: String,
    val clubId: Int,
    val guestReserveTime: String?,
    val impromptuId: Int,
    val introduction: String,
    val isGuest: Boolean,
    val isLeader: Boolean,
    val isValid: Boolean,
    val phoneNumber: String,
    val profileImage: String?,
    val sportsList: List<Int>
)

fun GetMemberDetailResultDto.toMemberDetail() : MemberDetail {
    return MemberDetail(
        id = accountId,
        userImage = profileImage,
        userName = accountNickname,
        userAgeGroup = convertAgeNumToString(ageNum),
        userArea = area,
        userPhoneNum = phoneNumber,
        userSports = sportsList.map { it-1 },
        intro = introduction,
        visitorDate = if(guestReserveTime != null) "*${dateDashToCol(guestReserveTime)} 에 손님신청을 했습니다." else null
    )
}

fun convertAgeNumToString (ageNum : Int) : String {
    return when(ageNum) {
        1 -> "10대"
        2-> "20대"
        3->"30대"
        4-> "40대"
        else-> "50대"
    }
}