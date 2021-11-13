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
fun StopWarningDialog (
    dialogOpenState : MutableState<Boolean>,
    stopSignupState : MutableState<Boolean>,
    textString : String
) {
    Dialog(
        onDismissRequest = { dialogOpenState.value = false },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {
        if(dialogOpenState.value){
            Column(
                modifier = Modifier
                    .width(292.dp)
                    .height(228.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 16.dp, bottom = 28.dp)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.End)) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                dialogOpenState.value = false
                            },
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "close icon"
                    )
                }
                Text(
                    text = stringResource(id = R.string.icon_warning_stop_signup),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = textString,
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .padding(horizontal = 12.dp)) {

                    WhiteRoundBtn(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .padding(end = 6.dp),
                        textString = stringResource(id = R.string.stop_signup),
                        fontSize = 14.sp,
                        textColor = R.color.main_black,
                        borderColor = R.color.gray03
                    ) {
                        dialogOpenState.value = false
                        stopSignupState.value = true

                    }

                    RoundBtn(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .padding(start = 6.dp),
                        btnColor = R.color.main_orange,
                        textString = R.string.continue_signup,
                        fontSize = 14.sp
                    ) {
                        dialogOpenState.value = false
                    }

                }

            }

        }

    }
}