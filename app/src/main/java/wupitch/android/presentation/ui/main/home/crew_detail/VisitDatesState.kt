package wupitch.android.presentation.ui.main.home.crew_detail

data class VisitDatesState(
    val isLoading : Boolean = false,
    val data : VisitorInfo? = null,
    val error : String = ""
)
