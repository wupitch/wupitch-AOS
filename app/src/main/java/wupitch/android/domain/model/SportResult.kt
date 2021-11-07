package wupitch.android.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


data class SportResult(
    val name: String,
    val sportsId: Int,
    val state : MutableState<Boolean> = mutableStateOf(false)
)