package wupitch.android.presentation.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.Resource
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.domain.model.OnboardingContent
import wupitch.android.presentation.theme.OnboardingTheme
import wupitch.android.presentation.theme.Roboto

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private val viewModel: OnboardingViewModel by viewModels()
    private lateinit var onboardingList: List<OnboardingContent>

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OnboardingTheme {
                    val kakaoLoginState by viewModel.kakaoLoginLiveData.observeAsState()
                    when(kakaoLoginState){
                        is Resource.Success -> findNavController().navigate(R.id.action_onboardingFragment_to_serviceAgreementFragment)
                        is Resource.Error -> {
                            Log.d("{OnboardingFragment.onCreateView}", "error!")
                        }
                        is Resource.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.fillMaxSize(), color = colorResource(
                                id = R.color.main_orange
                            ))
                        }
                    }
                    setOnboardingList()
                    val pagerState = rememberPagerState()
                    val scope = rememberCoroutineScope()

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.gray04))

                    ) {
                        val (indicator, pager, skip, kakaoBtn) = createRefs()

                        HorizontalPagerIndicator(
                            modifier = Modifier.constrainAs(indicator) {
                                top.linkTo(parent.top, margin= 32.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            pagerState = pagerState,
                            inactiveColor = colorResource(id = R.color.gray01),
                            activeColor = colorResource(id = R.color.main_black)
                        )
                        HorizontalPager(
                            modifier = Modifier.constrainAs(pager) {
                                top.linkTo(indicator.bottom, margin = 32.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, margin = 58.dp)
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
                                        .width(292.dp)
                                        .height(426.dp),
                                    painter = painterResource(id = onboardingList[page].imgDrawable),
                                    contentDescription = "pager image"
                                )
                            }
                        }

                        if(pagerState.currentPage != 3 || pagerState.currentPageOffset != 0f){
                            val annotatedString = buildAnnotatedString {
                                append(
                                    AnnotatedString(
                                        stringResource(id = R.string.skip_onboarding),
                                        spanStyle = SpanStyle(
                                            color = colorResource(id = R.color.gray02),
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Roboto,
                                            fontSize = 14.sp
                                        )
                                    )
                                )
                            }
                            ClickableText(modifier = Modifier
                                .constrainAs(skip) {
                                    end.linkTo(parent.end, margin = 16.dp)
                                    bottom.linkTo(parent.bottom, margin = 21.dp)
                                },
                                text = annotatedString,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(3)
                                    }
                                })

                        }else if(pagerState.currentPage ==3){
                           Image(
                               modifier = Modifier
                                   .constrainAs(kakaoBtn) {
                                       start.linkTo(parent.start, margin = 40.dp)
                                       end.linkTo(parent.end, margin = 40.dp)
                                       bottom.linkTo(parent.bottom, margin = 72.dp)
                                       width = Dimension.fillToConstraints
                                   }
                                   .clickable {
                                       //todo 카카오 로그인.
                                       signInWithKakao()
                                   },
                               painter = painterResource(id = R.drawable.kakao_login),
                               contentDescription = "kakao login button")
                        }

                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.kakaoLoginLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> findNavController().navigate(R.id.action_onboardingFragment_to_serviceAgreementFragment)
                is Resource.Error -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                is Resource.Loading -> {
                    //todo compose 에서 어떻게 loading dialog 보여주지???
                }
            }
        })
    }

    private fun setOnboardingList() {
        onboardingList = listOf<OnboardingContent>(
            OnboardingContent(
                getString(R.string.onboarding_title1),
                getString(R.string.onboarding_subtitle1),
                R.drawable.img_onbd_01
            ),
            OnboardingContent(
                getString(R.string.onboarding_title2),
                getString(R.string.onboarding_subtitle2),
                R.drawable.img_onbd_02
            ),
            OnboardingContent(
                getString(R.string.onboarding_title3),
                getString(R.string.onboarding_subtitle3),
                R.drawable.img_onbd_03
            ),
            OnboardingContent(
                getString(R.string.onboarding_title4),
                getString(R.string.onboarding_subtitle4),
                R.drawable.img_onbd_04
            )
        )
    }

    private fun signInWithKakao() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            //사용자 기기에 카카오톡 있는지 확인
            UserApiClient.instance.loginWithKakaoTalk(
                requireContext(),
                callback = kakaoCallback
            ) //카카오톡 실행
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoCallback
            ) //웹뷰 실행.
        }
    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("{OnboardingFragment.getKakaoUserInfo}", error.message.toString())
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                Log.d(
                    "{OnboardingFragment.getKakaoUserInfo}", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n성별: ${user.kakaoAccount?.gender?.name}"
                )
                val email = user.kakaoAccount?.email
                val genderType = user.kakaoAccount?.gender?.name
                val id = user.id
                val nickname = user.kakaoAccount?.profile?.nickname

                if(email != null && genderType != null && nickname != null) {
                    viewModel.postKakaoLogin(
                        KakaoLoginReq(
                            email = email,
                            genderType = genderType,
                            id = id,
                            nickname = nickname
                        )
                    )
                } else {
                    //todo 필수 제공 관련해서 다시 처리할 것.
                    Toast.makeText(requireContext(), "이메일, 성별, 닉네임 정보가 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("{OnboardingFragment.kakaoCallback}", "login error! ${error.message}")
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            Log.d("{OnboardingFragment.kakaoCallback}", "login success! ${token.accessToken}")
            getKakaoUserInfo()
        }
    }
}