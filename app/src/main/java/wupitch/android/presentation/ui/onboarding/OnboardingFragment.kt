package wupitch.android.presentation.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.Resource
import wupitch.android.domain.model.OnboardingContent
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var onboardingList: List<OnboardingContent>


    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {


                    val scope = rememberCoroutineScope()
                    val pagerState = rememberPagerState()

                    setOnboardingList()

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)

                    ) {
                        val (indicator, pager, skip) = createRefs()
                        val topGuideLine = createGuidelineFromTop(0.05f)
                        val bottomGuideLine = createGuidelineFromTop(0.9f)

                        HorizontalPagerIndicator(
                            modifier = Modifier.constrainAs(indicator) {
                                top.linkTo(topGuideLine)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            pagerState = pagerState,
                            inactiveColor = colorResource(id = R.color.gray01),
                            activeColor = colorResource(id = R.color.main_orange)
                        )
                        HorizontalPager(
                            modifier = Modifier.constrainAs(pager) {
                                top.linkTo(indicator.bottom, margin = 32.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomGuideLine, margin = 16.dp)
                            }, count = 4, state = pagerState
                        ) { page ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = onboardingList[page].title,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Roboto,
                                    color = colorResource(id = R.color.main_black),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    modifier = Modifier.padding(top = 8.dp),
                                    text = onboardingList[page].subtitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = Roboto,
                                    color = colorResource(id = R.color.gray02),
                                    textAlign = TextAlign.Center
                                )
                                Image(
                                    modifier = Modifier
                                        .padding(top = 32.dp)
                                        .width(264.dp)
                                        .height(419.dp),
                                    painter = painterResource(id = onboardingList[page].imgDrawable),
                                    contentDescription = "pager image"
                                )
                            }
                        }

                            ClickableText(modifier = Modifier
                                .constrainAs(skip) {
                                    top.linkTo(bottomGuideLine, margin = 6.dp)
                                    end.linkTo(parent.end, margin = 6.dp)
//                                    bottom.linkTo(parent.bottom, margin = 21.dp)
                                }
                                .padding(10.dp),
                                text = AnnotatedString(
                                    stringResource(id = R.string.skip_onboarding),
                                    spanStyle = SpanStyle(
                                        color = colorResource(id = R.color.main_orange),
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Roboto,
                                        fontSize = 14.sp
                                    )
                                ),
                                onClick = {
                                  findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
                                })
                    }

                }
            }
        }
    }

    private fun setOnboardingList() {
        onboardingList = listOf<OnboardingContent>(
            OnboardingContent(
                getString(R.string.onboarding_title1),
                getString(R.string.onboarding_subtitle1),
                R.drawable.onbd_001
            ),
            OnboardingContent(
                getString(R.string.onboarding_title2),
                getString(R.string.onboarding_subtitle2),
                R.drawable.onbd_002
            ),
            OnboardingContent(
                getString(R.string.onboarding_title3),
                getString(R.string.onboarding_subtitle3),
                R.drawable.onbd_003
            ),
            OnboardingContent(
                getString(R.string.onboarding_title4),
                getString(R.string.onboarding_subtitle4),
                R.drawable.onbd_004
            )
        )
    }

}