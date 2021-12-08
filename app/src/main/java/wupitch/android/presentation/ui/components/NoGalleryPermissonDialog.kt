package wupitch.android.presentation.ui.components


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
fun NoGalleryPermissionDialog(
    dialogOpenState: MutableState<Boolean>,
    gotoSettingClick : () -> Unit
) {
    Dialog(
        onDismissRequest = { dialogOpenState.value = false },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {
        if (dialogOpenState.value) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    text = stringResource(id = R.string.icon_warning_stop_signup),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = R.string.need_gallery_permission),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(36.dp))

                RoundBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 30.dp),
                    btnColor = R.color.main_orange,
                    textString = R.string.goto_setting,
                    fontSize = 14.sp
                ) {
                    dialogOpenState.value = false
                    gotoSettingClick()
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

        }

    }
}