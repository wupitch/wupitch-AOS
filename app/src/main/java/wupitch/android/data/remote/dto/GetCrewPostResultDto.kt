package wupitch.android.data.remote.dto


import com.google.gson.annotations.SerializedName
import wupitch.android.domain.model.CrewPostResult
import wupitch.android.util.dateDashToCol

data class GetCrewPostResultDto(
    val accountProfileImage: String,
    val contents: String,
    val date: String,
    val isAccountLike: Boolean,
    val isAccountReport: Boolean,
    val isCreator: Boolean,
    val isCreatorCrewLeader: Boolean,
    val isNotice: Boolean,
    val isPhotoPost: Boolean,
    val likeCount: Int,
    val nickname: String,
    val noticeTitle: String?,
    val postId: Int
)

fun GetCrewPostResultDto.toResult() : CrewPostResult {
    return CrewPostResult(
        id = postId,
        isAnnounce = isNotice,
        announceTitle = noticeTitle,
        userImage = accountProfileImage,
        isLeader = isCreatorCrewLeader,
        isLiked = isAccountLike,
        likedNum = likeCount,
        date = dateDashToCol(date),
        content = contents,
        userName = nickname
    )
}

