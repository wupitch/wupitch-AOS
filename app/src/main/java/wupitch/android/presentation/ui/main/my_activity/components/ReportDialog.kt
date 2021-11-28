package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.main.my_activity.MyActivityViewModel
import wupitch.android.presentation.ui.main.my_activity.MyCrewViewModel

@ExperimentalPagerApi
@Composable
fun ReportDialog(
    dialogOpen: MutableState<Boolean>,
    viewModel : ViewModel
) {
    Dialog(
        onDismissRequest = { dialogOpen.value = false },
        DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true),
    ) {

        if (dialogOpen.value) {
            val textState = remember { mutableStateOf("")}
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(top = 12.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.End)){
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
                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
                    text = stringResource(id = R.string.report),
                    color = Color.Black,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )

                ReportTextField(
                    textState = textState,
                    hintText = stringResource(id = R.string.input_report),
                    maxLength = 45,
                )
                
                RoundBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 23.dp)
                        .padding(horizontal = 24.dp)
                        .height(48.dp),
                    btnColor = R.color.main_orange,
                    textString = R.string.done,
                    fontSize = 14.sp
                ) {
                    if(viewModel is MyCrewViewModel){
                        viewModel.postCrewReport(textState.value)
                    }
                    dialogOpen.value = false
                }
            }
        }
    }
}