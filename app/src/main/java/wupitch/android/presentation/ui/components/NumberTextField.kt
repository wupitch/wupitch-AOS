package wupitch.android.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@ExperimentalPagerApi
@Composable
fun NumberTextField(
    modifier: Modifier,
    textState: MutableState<String>,
    thousandIndicator : Boolean = false,
    hintString : String
) {

    BasicTextField(
        value = textState.value,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        maxLines = 1,
        onValueChange = { value ->
            if(thousandIndicator){
                //todo 천 단위에 점 찍기.
                if(value.length <=6){
                    textState.value = value
                }
            }else {
                if(value.length <=3){
                    textState.value = value
                }
            }

        },
        cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(id = R.color.gray04))
                    .padding(horizontal = 16.dp)
                    .padding(top = 11.dp, bottom = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    val (hint) = createRefs()

                    innerTextField()
                    if (textState.value.isEmpty()) {
                        Text(
                            modifier = Modifier.constrainAs(hint) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                            text = hintString,
                            color = colorResource(id = R.color.gray03),
                            fontSize = 16.sp,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    )
}