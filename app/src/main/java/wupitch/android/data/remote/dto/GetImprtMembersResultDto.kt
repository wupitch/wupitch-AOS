package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName
import wupitch.android.domain.model.CrewMember
import wupitch.android.domain.model.ImprtMember

data class GetImprtMembersResultDto(
    val accountId: Int,
    val accountNickname: String,
    val addedAt: String,
    val isLeader: Boolean,
    val isValid: Boolean
)

fun GetImprtMembersResultDto.toImprtMember() : ImprtMember {
    return ImprtMember(
        id = accountId,
        userName = accountNickname,
        userImage = null,
        isLeader = isLeader,
        isValid = isValid
    )
}