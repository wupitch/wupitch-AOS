package wupitch.android.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class FilterItem(
    val name : Int,
    val state : MutableState<Boolean> = mutableStateOf(false)
)
