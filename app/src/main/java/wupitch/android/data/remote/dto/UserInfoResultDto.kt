package wupitch.android.data.remote.dto


import wupitch.android.domain.model.UserInfoResult

data class UserInfoResultDto(
    val accountId: Int,
    val email: String,
    val introduce: String?,
    val isPushAgree: Boolean?,
    val jwt: String,
    val nickname: String,
    val profileImageUrl: String?
)

fun UserInfoResultDto.toResult() : UserInfoResult {
    return UserInfoResult(
        introduce = introduce ?: "",
        isPushAgree = isPushAgree ?: false,
        nickname = nickname,
        profileImageUrl = profileImageUrl ?: ""
    )
}