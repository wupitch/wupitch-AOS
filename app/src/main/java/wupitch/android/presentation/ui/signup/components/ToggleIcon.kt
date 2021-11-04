package wupitch.android.presentation.ui.signup.components

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun ToggleIcon(
    toggleState: MutableState<Boolean>,
    modifier: Modifier,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes textString: Int,
    onDetailClick: (() -> Unit)?
) {
    ConstraintLayout(modifier = modifier) {

        val (icon, text, detailTextBtn) = createRefs()

        Icon(
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(24.dp)
                .toggleable(
                    value = toggleState.value,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = true,
                    role = Role.Checkbox,
                    onValueChange = {
                        onCheckedChange(it)
                    }
                ),
            painter = painterResource(id = R.drawable.check),
            tint = if (toggleState.value) colorResource(id = R.color.main_orange) else colorResource(
                id = R.color.gray03
            ),
            contentDescription = "toggle button"
        )

        Text(
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(icon.end, margin = 8.dp)
            },
            text = stringResource(id = textString),
            fontSize = 14.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            color = colorResource(id = R.color.main_black)
        )

        if (onDetailClick != null) {
            val annotatedString = buildAnnotatedString {
                append(
                    AnnotatedString(
                        stringResource(id = R.string.detail_agreement),
                        spanStyle = SpanStyle(
                            color = colorResource(id = R.color.gray03),
                            fontWeight = FontWeight.Normal,
                            fontFamily = Roboto,
                            fontSize = 12.sp
                        )
                    )
                )
            }
            ClickableText(modifier = Modifier.constrainAs(detailTextBtn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
                text = annotatedString,
                onClick = {
                    onDetailClick()
                })
        }
    }
}