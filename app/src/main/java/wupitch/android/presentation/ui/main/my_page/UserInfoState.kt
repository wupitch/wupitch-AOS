package wupitch.android.presentation.ui.main.my_page

import wupitch.android.domain.model.UserInfoResult

data class UserInfoState(
    val isLoading : Boolean = false,
    val data : UserInfoResult = UserInfoResult(
        introduce = "",
        isPushAgree = false,
        nickname = "",
        profileImageUrl = ""
    ),
    val error : String = ""
)
