package wupitch.android.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import wupitch.android.R

@Composable
fun PinToggleButton(
    modifier: Modifier,
    toggleState : MutableState<Boolean>,
    onValueChange : (Boolean) -> Unit,
) {
    Image(painter = if(toggleState.value) painterResource(id = R.drawable.ic_pin)
        else painterResource(id = R.drawable.pin_inact),
        contentDescription = "crew detail pin",
        modifier = modifier.size(24.dp).toggleable(
            value = toggleState.value,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = true,
            role = Role.Checkbox,
            onValueChange = onValueChange
        )
    )
}