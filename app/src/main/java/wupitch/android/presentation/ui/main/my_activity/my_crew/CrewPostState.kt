package wupitch.android.presentation.ui.main.my_activity.my_crew

data class CrewPostState(
    val isLoading : Boolean = false,
    val data : List<CrewPost> = listOf(),
    val error : String = ""
)
