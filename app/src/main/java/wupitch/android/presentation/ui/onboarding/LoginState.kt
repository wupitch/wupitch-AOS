package wupitch.android.presentation.ui.onboarding

data class LoginState(
    val isLoading : Boolean = false,
    val isSuccess : Boolean = false,
    val error : String = ""
)
