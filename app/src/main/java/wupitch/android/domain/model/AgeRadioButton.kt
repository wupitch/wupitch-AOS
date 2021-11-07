package wupitch.android.domain.model

import androidx.compose.runtime.MutableState

data class AgeRadioButton(
    val ageString : String,
    val checkedState : MutableState<Boolean>
)
