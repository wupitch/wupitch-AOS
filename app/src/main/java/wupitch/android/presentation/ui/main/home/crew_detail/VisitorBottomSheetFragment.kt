package wupitch.android.presentation.ui.main.home.crew_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.RoundBtn


class VisitorBottomSheetFragment(
    val visitorCost: String,
    val visitDateList: List<String>
) : BottomSheetDialogFragment() {

    private lateinit var selectedDate: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            setContent {

                val selectedOption = remember {
                    mutableStateOf("")
                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .height(479.dp)
                        .height(IntrinsicSize.Max)
                        .padding(top = 22.dp, bottom = 42.dp)
                        .padding(horizontal = 20.dp)
                ) {

                    val (title, close, divider, step1Row, step2Box, step2Row, subtitle, step3Box, step3Row, radioGroup, registerBtn) = createRefs()

                    Text(
                        modifier = Modifier.constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                        text = stringResource(id = R.string.join_visitor_bottom_sheet_title),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.main_black)
                    )

                    Image(
                        modifier = Modifier
                            .constrainAs(close) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                            .size(24.dp)
                            .clickable {
                                dismiss()
                            },
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "close icon"
                    )


                    Divider(
                        modifier = Modifier
                            .constrainAs(divider) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(title.bottom)
                            }
                            .fillMaxWidth()
                            .padding(top = 25.dp),
                        color = colorResource(id = R.color.gray03),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier
                            .constrainAs(step1Row) {
                                start.linkTo(parent.start)
                                top.linkTo(divider.bottom)
                            }
                            .padding(top = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(23.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.one),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = stringResource(id = R.string.join_visitor_step1),
                            color = colorResource(id = R.color.main_black),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Roboto
                        )
                    }


                    Box(
                        modifier = Modifier
                            .constrainAs(step2Box) {
                                start.linkTo(parent.start)
                                top.linkTo(step1Row.bottom, margin = 20.dp)
                            }
                            .size(23.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.two),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.constrainAs(step2Row) {
                            start.linkTo(step2Box.end, margin = 10.dp)
                            top.linkTo(step2Box.top)
                            bottom.linkTo(step2Box.bottom)
                        }, verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(id = R.string.join_visitor_step2),
                            color = colorResource(id = R.color.main_black),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Roboto
                        )
                        Text(
                            text = visitorCost,
                            color = colorResource(id = R.color.main_orange),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Roboto
                        )
                        Text(
                            text = stringResource(id = R.string.end_sentence),
                            color = colorResource(id = R.color.main_black),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Roboto
                        )
                    }
                    Text(
                        modifier = Modifier
                            .constrainAs(subtitle) {
                                start.linkTo(step2Row.start)
                                top.linkTo(step2Row.bottom, margin = 4.dp)
                            },
                        text = stringResource(id = R.string.join_visitor_step2_subtitle),
                        color = colorResource(id = R.color.gray02),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Roboto
                    )


                    Box(
                        modifier = Modifier
                            .constrainAs(step3Box) {
                                start.linkTo(parent.start)
                                top.linkTo(subtitle.bottom, margin = 23.dp)
                            }
                            .size(23.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.three),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        modifier = Modifier.constrainAs(step3Row) {
                            start.linkTo(step3Box.end, margin = 10.dp)
                            top.linkTo(step3Box.top)
                            bottom.linkTo(step3Box.bottom)
                        },
                        text = stringResource(id = R.string.join_visitor_step3),
                        color = colorResource(id = R.color.main_black),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Roboto
                    )
                    Column(modifier = Modifier.constrainAs(radioGroup) {
                        top.linkTo(step3Row.bottom)
                        start.linkTo(step3Row.start)
                    }) {
                        selectedDate = RadioGroup(selectedOption)
                    }

                    RoundBtn(
                        modifier = Modifier
                            .constrainAs(registerBtn) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(radioGroup.bottom, margin = 20.dp)
                            }
                            .fillMaxWidth()
                            .height(52.dp),
                        btnColor = if(selectedOption.value == "") R.color.gray03 else R.color.main_black,
                        textString = R.string.register,
                        fontSize = 16.sp
                    ) {
                        if(selectedOption.value != ""){
                            dismiss()
                            //todo 손님신청 server connection. 성공시 손님 신청 완료 dialog
                            //todo viewmodel 에 selectedDate 보내기.
                            Log.d("{VisitorBottomSheetFragment.onCreateView}", selectedDate)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RadioGroup(
        selectedOption : MutableState<String>
    ): String {
        if (visitDateList.isNotEmpty()) {


            Column(modifier = Modifier.padding(top = 16.dp)) {
                visitDateList.forEach { item ->
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.size(24.dp),
                            selected = (item == selectedOption.value),
                            onClick = {
                                selectedOption.value = item
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(id = R.color.main_orange),
                                unselectedColor = colorResource(id = R.color.gray03)
                                )
                        )

                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = colorResource(id = R.color.main_black),
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = Roboto,
                                    fontSize = 14.sp
                                )
                            ) { append(item) }
                        }

                        ClickableText(modifier = Modifier.padding(start = 8.dp),
                            text = annotatedString, onClick = { selectedOption.value = item })
                    }
                }
            }
            return selectedOption.value
        } else {
            return ""
        }
    }
}
