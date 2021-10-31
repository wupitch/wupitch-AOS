package wupitch.android.presentation.ui.onboarding

data class KakaoLoginState(
    var isLoading : Boolean = false,
    var isSuccess : Boolean = false,
    var error : String = ""
)
