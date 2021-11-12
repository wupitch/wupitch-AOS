package wupitch.android.presentation.ui.main.home.create_crew

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.NonRepetitionLayout
import wupitch.android.presentation.ui.components.RoundBtn
import wupitch.android.presentation.ui.components.TitleToolbar

@AndroidEntryPoint
class CreateCrewSportFragment : Fragment() {


    private val viewModel : CreateCrewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {


            setContent {

                val sportSelectedState = remember {
                    mutableStateOf(-1)
                }

                val sportsList = viewModel.sportsList.value

                ConstraintLayout(
                    Modifier
                        .background(Color.White)
                        .fillMaxSize()) {
                    val (toolbar, divider, content, nextBtn, progressBar) =  createRefs()

                    TitleToolbar(modifier = Modifier.constrainAs(toolbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, textString = R.string.create_crew) {
                        findNavController().navigateUp()
                    }

                    Divider(
                        Modifier
                            .constrainAs(divider) {
                                top.linkTo(toolbar.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                            .width(1.dp)
                            .background(colorResource(id = R.color.gray01)))

                    if(sportsList.isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier.constrainAs(progressBar){
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(toolbar.bottom)
                                bottom.linkTo(nextBtn.top)
                            },
                            color = colorResource(id = R.color.main_orange))
                    }

                    if(sportsList.data.isNotEmpty()){
                        Log.d("{CreateCrewSportFragment.onCreateView}", sportsList.data.toString())
                        Column(Modifier.constrainAs(content){
                            top.linkTo(divider.bottom, margin = 24.dp)
                            start.linkTo(parent.start, margin = 20.dp)
                        }) {
                            Text(
                                text = stringResource(id = R.string.select_crew_sport),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.main_black),
                                fontSize = 20.sp,
                            )
//                            Spacer(modifier = Modifier.height(32.dp))
                            NonRepetitionLayout(
                                filterItemList = sportsList.data,
                                flexBoxModifier = Modifier.padding(top = 32.dp),
                                radioBtnModifier = Modifier
                                    .width(96.dp)
                                    .height(48.dp),
                                selectedState = sportSelectedState
                            ){
                                Log.d("{CreateCrewSport.onCreateView}", "스포츠 : $it")
                            }
                        }
                    }

                    //todo 동 state 추가.
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
                        btnColor = if (sportSelectedState.value != -1) R.color.main_orange
                        else R.color.gray03,
                        textString = R.string.one_over_seven,
                        fontSize = 16.sp
                    ) {
                        if (sportSelectedState.value != -1) {
                            Log.d("{CreateCrewSport.onCreateView}", "next btn clicked!")
                            //todo viewmodel 에 선택된 sport 보내기.
                            findNavController().navigate(R.id.action_createCrewSport_to_createCrewLocationFragment)
                        }
                    }


                }


            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSports()
    }
}