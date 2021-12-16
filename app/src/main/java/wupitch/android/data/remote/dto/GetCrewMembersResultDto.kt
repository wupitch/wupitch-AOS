package wupitch.android.data.remote.dto


import wupitch.android.domain.model.CrewMember

data class GetCrewMembersResultDto(
    val accountId: Int,
    val accountNickname: String,
    val addedAt: String,
    val guestReserveTime: String,
    val isGuest: Boolean,
    val isLeader: Boolean,
    val isValid: Boolean
)

fun GetCrewMembersResultDto.toCrewMember() : CrewMember {
    return CrewMember(
        id = accountId,
        userName = accountNickname,
        userImage = null,
        isLeader = isLeader,
        isGuest = isGuest,
        isValid = isValid
    )
}

