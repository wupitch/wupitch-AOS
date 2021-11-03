package wupitch.android.presentation.ui.main.crew_detail.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.RoundBtn

@Composable
fun NotEnoughInfoDialog(
    dialogOpen: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = { dialogOpen.value = false },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {

        if (dialogOpen.value) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .height(266.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 12.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(end = 12.dp).align(Alignment.End)){
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
                Text(text = stringResource(id = R.string.crying_emoji), fontSize = 24.sp)
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(id = R.string.not_enough_info_title),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.padding(top =8.dp),
                    text = stringResource(id = R.string.not_enough_info_subtitle),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                RoundBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 36.dp)
                        .padding(horizontal = 30.dp),
                    btnColor = R.color.main_orange,
                    textString = R.string.go_fill_profile,
                    fontSize = 14.sp
                ) {
                    //todo : profile page 로 이동
                }

            }
        }
    }
}