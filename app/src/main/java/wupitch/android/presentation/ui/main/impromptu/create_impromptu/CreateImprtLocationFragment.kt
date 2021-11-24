package wupitch.android.presentation.ui.main.impromptu.create_impromptu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

@AndroidEntryPoint
class CreateImprtLocationFragment : Fragment() {

    private lateinit var districtBottomSheet: DistrictBottomSheetFragment
    private val viewModel: CreateImprtViewModel by activityViewModels()

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if(stopSignupState.value) {
                        findNavController().navigate(R.id.action_createCrewLocationFragment_to_mainFragment)
                    }
                    if(dialogOpenState.value){
                        StopWarningDialog(dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_crew_warning))
                    }


                    val locationTextState = remember { mutableStateOf(viewModel.crewLocation.value) }
                    val districtList = viewModel.districtList.value
                    val districtState = viewModel.imprtDistrictName.observeAsState()


                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, divider, content, nextBtn, progressBar) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { dialogOpenState.value = true },
                            textString = R.string.create_crew
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(divider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        if (districtList.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressBar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(nextBtn.top)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }

                        if (districtList.data.isNotEmpty()) {

                            Column(
                                Modifier
                                    .constrainAs(content) {
                                        top.linkTo(divider.bottom, margin = 24.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .padding(horizontal = 20.dp)) {
                                Text(
                                    text = stringResource(id = R.string.location_of_crew),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.main_black),
                                    fontSize = 20.sp,
                                    lineHeight = 28.sp
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                 WhiteRoundBtn(
                                        modifier = Modifier
                                            .height(48.dp)
                                            .width(152.dp),
                                        textString = if (districtState.value != null) districtState.value!! else stringResource(id = R.string.select_region_btn),
                                        fontSize = 14.sp,
                                        textColor = if (districtState.value != null) R.color.main_orange else R.color.gray02,
                                        borderColor = if (districtState.value != null) R.color.main_orange else R.color.gray02
                                    ) {
                                        showDistrictBottomSheet(districtList.data)
                                    }

                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = stringResource(id = R.string.location),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.main_black),
                                    fontSize = 16.sp,
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                SimpleTextField(
                                    textState = locationTextState,
                                    hintText = stringResource(id = R.string.input_location),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        setKeyboardDown()
                                    })
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = R.string.empty_location_warning),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal,
                                    color = colorResource(id = R.color.gray02),
                                    fontSize = 14.sp,
                                )
                            }
                        }


                        RoundBtn(
                            modifier = Modifier
                                .constrainAs(nextBtn) {
                                    bottom.linkTo(parent.bottom, margin = 24.dp)
                                    start.linkTo(parent.start, margin = 20.dp)
                                    end.linkTo(parent.end, margin = 20.dp)
                                    width = Dimension.fillToConstraints
                                }
                                .fillMaxWidth()
                                .height(52.dp),
                            btnColor = if (districtState.value != null) R.color.main_orange else R.color.gray03,
                            textString = R.string.two_over_seven,
                            fontSize = 16.sp
                        ) {
                            if (districtState.value != null) {
                                viewModel.setImprtLocation(locationTextState.value)
                                findNavController().navigate(R.id.action_createCrewLocationFragment_to_createCrewInfoFragment)
                            }
                        }
                    }
                }
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDistricts()
    }

    private fun showDistrictBottomSheet(districtList: Array<String>) {
        districtBottomSheet = DistrictBottomSheetFragment(districtList, viewModel)
        districtBottomSheet.show(childFragmentManager, "district_bottom_sheet")
    }

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}