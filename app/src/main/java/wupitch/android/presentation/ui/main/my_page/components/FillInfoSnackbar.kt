package wupitch.android.presentation.ui.main.my_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@Composable
fun FillInfoSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier
) {
    SnackbarHost(
        modifier = modifier,
        hostState = snackbarHostState,
        snackbar = {
            ConstraintLayout(
            ) {
                val (background, text) = createRefs()

                Image(
                    modifier = Modifier.constrainAs(background){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }.size(180.dp, 71.dp),
                    painter = painterResource(id = R.drawable.bubble),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.constrainAs(text){
                        start.linkTo(background.start)
                        end.linkTo(background.end)
                        bottom.linkTo(background.bottom, margin = 12.dp)
                    },
                    text = snackbarHostState.currentSnackbarData?.message ?: "",
                    color = Color.White,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        })
}