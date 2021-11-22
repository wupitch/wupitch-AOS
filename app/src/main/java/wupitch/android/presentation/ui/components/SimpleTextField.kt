package wupitch.android.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto

@ExperimentalPagerApi
@Composable
fun SimpleTextField(
    textState: MutableState<String>,
    hintText: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = colorResource(id = R.color.gray03),
        backgroundColor = colorResource(id = R.color.gray03)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            value = textState.value,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = 1,
            onValueChange = { value ->
                textState.value = value
            },
            cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
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
                                text = hintText,
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
}