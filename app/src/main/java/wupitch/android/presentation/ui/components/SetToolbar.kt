package wupitch.android.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun SetToolBar(
    modifier: Modifier,
    onLeftIconClick: () -> Unit,
    onRightIconClick: (() -> Unit)? = null,
    @StringRes textString: Int?,
    hasRightIcon: Boolean? = false
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        val (leftIcon, text, rightIcon) = createRefs()

        Icon(modifier = Modifier
            .constrainAs(leftIcon) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(text.start)
            }
            .size(24.dp)
            .clickable(interactionSource = MutableInteractionSource(), indication = null)
            //interaction source??
            { onLeftIconClick() },
            painter = painterResource(id = R.drawable.left),
            contentDescription = "go back previous page"
        )


        if (textString != null) {
            Text(
                modifier = modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(leftIcon.end, margin = 32.dp)
                },
                text = stringResource(id = textString),
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }

        hasRightIcon?.let {
            if(it) {
                Icon(modifier = Modifier.constrainAs(rightIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                    .size(24.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    )
                    //interaction source??
                    {
                        if (onRightIconClick != null) {
                            onRightIconClick()
                        }
                    },
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "close this page"
                )
            }
        }
    }
}