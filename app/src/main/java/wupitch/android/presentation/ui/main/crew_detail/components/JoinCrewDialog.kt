package wupitch.android.presentation.ui.main.crew_detail.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.OrangeRoundBtn

@Composable
fun JoinCrewDialog(
    dialogOpen: MutableState<Boolean>,
    @StringRes titleString : Int
) {
    Dialog(
        onDismissRequest = { dialogOpen.value = false },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {

        if (dialogOpen.value) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .height(236.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 36.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.firecracker), fontSize = 24.sp)
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = titleString),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                OrangeRoundBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 36.dp)
                        .padding(horizontal = 30.dp),
                    textString = R.string.confirmed,
                    fontSize = 14.sp
                ) {
                    dialogOpen.value = false
                }

            }
        }
    }
}