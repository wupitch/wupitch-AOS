package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.WhiteRoundBtn

@Composable
fun ImageShareDialog(
    dialogOpen: MutableState<Boolean>,
    onShareClick: (shareOnlyToCrew : Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = {
            dialogOpen.value = false
        },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {

        if (dialogOpen.value) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(228.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 12.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.End)
                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                dialogOpen.value = false
                            },
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "close icon"
                    )
                }
                Text(text = stringResource(id = R.string.camera_emoji), fontSize = 24.sp)
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = R.string.image_share_dialog_title),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(36.dp))

                Row(Modifier.fillMaxWidth().height(40.dp).padding(horizontal = 20.dp)) {
                    WhiteRoundBtn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        textString = stringResource(id = R.string.image_share_only_to_crew),
                        fontSize = 14.sp,
                        textColor = R.color.main_orange,
                        borderColor = R.color.main_orange
                    ) {
                        onShareClick(true)
                        dialogOpen.value = false
                    }
                    Spacer(modifier = Modifier.width(12.dp))

                    RoundBtn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        btnColor = R.color.main_orange,
                        textString = R.string.image_share_to_feed,
                        fontSize = 14.sp
                    ) {
                        onShareClick(false)
                        dialogOpen.value = false
                    }

                }
            }
        }
    }
}