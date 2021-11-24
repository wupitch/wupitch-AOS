package wupitch.android.presentation.ui.main.home.create_crew

data class CreateCrewState(
    val isLoading : Boolean = false,
    val data : Int? = null,
    val error : String = ""
)
