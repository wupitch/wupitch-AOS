package wupitch.android.data.remote.dto


import wupitch.android.domain.model.CrewMember

data class GetCrewMembersResultDto(
    val accountId: Int,
    val accountNickname: String,
    val addedAt: String,
    val guestReserveTime: String,
    val isGuest: Boolean,
    val isLeader: Boolean,
    val isValid: Boolean?,
    val profileImage : String?
)

fun GetCrewMembersResultDto.toCrewMember() : CrewMember {
    return CrewMember(
        id = accountId,
        userName = accountNickname,
        userImage = profileImage,
        isLeader = isLeader,
        isValid = isValid
    )
}

