package wupitch.android.presentation.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun NotiToolbar(
    modifier: Modifier,
    textString : String,
    onNotiClick : () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (icon_notification, text) = createRefs()

                Text(
                    text = textString,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                    color = colorResource(id = R.color.black),
                    fontSize = 22.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    modifier = Modifier
                        .constrainAs(icon_notification) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .size(24.dp),
                    onClick = {
                        onNotiClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bell),
                        contentDescription = "search"
                    )
                }

            }
        }
    }
}