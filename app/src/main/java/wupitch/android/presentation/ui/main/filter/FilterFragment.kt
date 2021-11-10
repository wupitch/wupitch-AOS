package wupitch.android.presentation.ui.main.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.SetToolBar
import wupitch.android.presentation.ui.components.TitleToolbar
import wupitch.android.presentation.ui.components.WhiteRoundBtn
import wupitch.android.presentation.ui.main.crew_detail.VisitorBottomSheetFragment

class FilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    ConstraintLayout(Modifier.fillMaxSize()) {

                        val (toolbar, topDivider, bottomDivider, buttons) = createRefs()
                        TitleToolbar(
                            modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            textString = R.string.filter
                        ){
                            findNavController().navigateUp()
                        }
                        Divider(
                            Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )
                        Divider(
                            Modifier
                                .constrainAs(bottomDivider) {
                                    bottom.linkTo(buttons.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )
                        FilterBottomButtons(
                            modifier = Modifier.constrainAs(buttons) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )


                    }


                }
            }
        }
    }



    @Composable
    fun FilterBottomButtons(
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
                .height(68.dp)
                .padding(vertical = 12.dp)
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            val (init, apply) = createRefs()

            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .background(Color.White)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            //todo 초기화.
                        }
                    ), contentAlignment = Alignment.Center
//                    .constrainAs(init) {
//                        start.linkTo(parent.start)
//                        end.linkTo(apply.start)
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                    }


            ) {
                Text(
                    text = stringResource(id = R.string.initialize),
                    fontSize = 14.sp, fontFamily = Roboto, fontWeight = FontWeight.Normal,
                    color = colorResource(id = R.color.gray02)
                )
            }

            RoundBtn(
                modifier = Modifier
                    .weight(0.65f)
//                    .constrainAs(apply) {
//                        end.linkTo(parent.end)
////                        top.linkTo(parent.top)
////                        bottom.linkTo(parent.bottom)
////                        start.linkTo(init.end)
//                        width = Dimension.fillToConstraints
//                    }
                    .width(208.dp)
                    .height(44.dp),
                btnColor = R.color.main_orange,
                textString = R.string.apply, fontSize = 16.sp
            ) {
                //todo apply button function.
            }


        }
    }
}