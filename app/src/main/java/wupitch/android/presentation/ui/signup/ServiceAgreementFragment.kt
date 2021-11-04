package wupitch.android.presentation.ui.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import okhttp3.internal.wait
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentServiceAgreementBinding
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.SetToolBar
import wupitch.android.presentation.ui.signup.components.ToggleIcon

class ServiceAgreementFragment : Fragment() {

    private var agreementSatisfiedNum = 0
    private var nonAllButtonClicked = true
    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var stopSignupDialog: StopSignupDialog
    private val viewModel: SignupViewModel by activityViewModels()

//    override fun onResume() {
//        super.onResume()
//        checkIfAgreementSatisfied()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, titleTop, titleBottom, subtitle, allToggleBtn, grayCol, NextBtn) = createRefs()
                        SetToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onClick = {
                            Log.d("{ServiceAgreementFragment.onCreateView}", "navigate up")
                        }, textString = null)

                        Row(modifier = Modifier.constrainAs(titleTop) {
                            top.linkTo(toolbar.bottom, margin = 32.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                        }) {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_orange),
                                fontSize = 22.sp
                            )
                            Text(
                                text = stringResource(id = R.string.service_agreement_title_top),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 22.sp
                            )
                        }

                        Text(
                            modifier = Modifier.constrainAs(titleBottom) {
                                top.linkTo(titleTop.bottom, margin = 4.dp)
                                start.linkTo(titleTop.start)
                            },
                            text = stringResource(id = R.string.service_agreement_title_bottom),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.main_black),
                            fontSize = 22.sp
                        )
                        Text(
                            modifier = Modifier.constrainAs(subtitle) {
                                top.linkTo(titleBottom.bottom, margin = 7.dp)
                                start.linkTo(titleBottom.start)
                            },
                            text = stringResource(id = R.string.service_agreement_subtitle),
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.gray02),
                            fontSize = 14.sp
                        )

                        val allToggleState = remember {
                            mutableStateOf(false)
                        }

                        ToggleIcon(
                            toggleState = allToggleState,
                            modifier = Modifier
                                .constrainAs(allToggleBtn) {
                                    start.linkTo(subtitle.start)
                                    top.linkTo(subtitle.bottom, margin = 58.dp)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                },
                            onCheckedChange = {
                                allToggleState.value = it
                                Log.d(
                                    "{ServiceAgreementFragment.onCreateView}",
                                    it.toString()
                                )
                            },
                            textString = R.string.agree_all,
                            onDetailClick = null
                        )

                        Column(
                            modifier = Modifier
                                .constrainAs(grayCol) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(allToggleBtn.bottom, margin = 16.dp)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                }
                                .background(colorResource(id = R.color.gray04))
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp)
                        ) {

                            ToggleIcon(
                                toggleState = allToggleState,
                                modifier = Modifier.fillMaxWidth(),
                                onCheckedChange = {
                                    allToggleState.value = it
                                    Log.d(
                                        "{ServiceAgreementFragment.onCreateView}",
                                        it.toString()
                                    )
                                },
                                textString = R.string.terms_of_service_agreement,
                                onDetailClick = {

                                }
                            )

                        }
                    }


                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setTitleText()

    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        backPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if(viewModel.userRegion.value != null) showStopSignupDialog()
////                else return 이때는 왜 아무것도 없지? todo : onboading activity 와 연결되었을 때 확인해볼것!!!
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
//    }
//
//    fun checkOkayToStopSignup() {
//        if(viewModel.userRegion.value != null) showStopSignupDialog()
//        else activity?.finish()
//    }
//
//    private fun showStopSignupDialog() {
//        stopSignupDialog = StopSignupDialog(requireContext(), this)
//        stopSignupDialog.show()
//    }
//
//    override fun onStopSignupClick() {
//        stopSignupDialog.dismiss()
//        activity?.finish()
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        backPressedCallback.remove()
//    }
//
//    val checkedChangeListener =
//        CompoundButton.OnCheckedChangeListener { _, isChecked ->
//            if (isChecked) ++agreementSatisfiedNum
//            else --agreementSatisfiedNum
//        }
//
//    fun setAllButtonClickListener() {
//        nonAllButtonClicked = false
//        if (!binding.toggleAgreeAll.isChecked && !nonAllButtonClicked) {
//            binding.btnNext.isActivated = false
//            binding.toggleServiceAgreement.isChecked = false
//            binding.togglePrivacy.isChecked = false
//            binding.togglePushNotification.isChecked = false
//        } else {
//            binding.btnNext.isActivated = true
//            binding.toggleServiceAgreement.isChecked = true
//            binding.togglePrivacy.isChecked = true
//            binding.togglePushNotification.isChecked = true
//        }
//        checkIfAgreementSatisfied()
//    }
//
//    fun setNonAllButtonClickListener() {
//        nonAllButtonClicked = true
//        checkIfAgreementSatisfied()
//    }
//
//    private fun checkIfAgreementSatisfied() {
//        binding.toggleAgreeAll.isChecked = agreementSatisfiedNum >= 3
//        binding.btnNext.isActivated =
//            (binding.toggleServiceAgreement.isChecked && binding.togglePrivacy.isChecked) || binding.toggleAgreeAll.isChecked
//    }
//

//
//    fun checkForNavigationToRegion(view: View) {
//        if (view.isActivated) Navigation.findNavController(view)
//            .navigate(R.id.action_serviceAgreementFragment_to_regionFragment)
//    }
//
//    fun showServiceAgreementDetailFragment(view: View) {
//        view.findNavController()
//            .navigate(R.id.action_serviceAgreementFragment_to_serviceAgreementDetailFragment)
//    }
//
//
//    fun showUseOfPersonalInfoDetailFragment(view: View) {
//        view.findNavController()
//            .navigate(R.id.action_serviceAgreementFragment_to_useOfPersonalInfoDetailFragment)
//    }
}