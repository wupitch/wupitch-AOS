package wupitch.android.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun NoToggleLayout(
    toggleState: MutableState<Boolean>,
    textString: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            modifier = Modifier
                .size(24.dp)
                .toggleable(
                    value = toggleState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.Checkbox,
                    onValueChange = {
                        toggleState.value = it
                    }
                ),
            painter = painterResource(id = R.drawable.check),
            tint = if (toggleState.value) colorResource(id = R.color.main_orange) else colorResource(
                id = R.color.gray03
            ),
            contentDescription = "toggle button"
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.toggleable(
                value = toggleState.value,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = true,
                role = Role.Checkbox,
                onValueChange = {
                    toggleState.value = it
                }
            ),
            text = textString,
            fontSize = 14.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = if (toggleState.value) colorResource(id = R.color.main_orange) else colorResource(
                id = R.color.gray02
            )
        )
    }
}