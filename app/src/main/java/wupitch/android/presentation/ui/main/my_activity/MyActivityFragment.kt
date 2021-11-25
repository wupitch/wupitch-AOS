package wupitch.android.presentation.ui.main.my_activity

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._wupitch_android_presentation_ui_main_home_HomeFragment_GeneratedInjector
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.main.MainFragment
import wupitch.android.presentation.ui.main.impromptu.components.ImpromptuCard
import wupitch.android.presentation.ui.main.my_activity.components.MyCrewCard

@AndroidEntryPoint
class MyActivityFragment : Fragment() {

    private val viewModel : MyActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMyImpromptu()
        viewModel.getMyCrew()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val myImprtState = remember {viewModel.myImprtState}
                    val myCrewState = remember {viewModel.myCrewState}

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {

                        val (toolbar, colList) = createRefs()

                        MyActivityToolbar(
                            modifier = Modifier.constrainAs(toolbar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        )

                        MyActivityScrollCol(
                            modifier = Modifier
                                .constrainAs(colList) {
                                    top.linkTo(toolbar.bottom)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                    height = Dimension.fillToConstraints
                                },
                            myCrewState = myCrewState,
                            myImprtState = myImprtState
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun MyActivityScrollCol(
        modifier: Modifier,
        myCrewState : State<MyCrewState>,
        myImprtState : State<MyImprtState>
        ) {
        LazyColumn(modifier = modifier) {
            item {

                Text(
                    modifier = Modifier.padding(top = 8.dp, start = 20.dp),
                    text = stringResource(id = R.string.crew_in_activity),
                    fontFamily = Roboto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(myCrewState.value.isLoading){
                item {
                    Box(modifier = Modifier.height(160.dp).fillMaxWidth(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.main_orange)
                        )
                    }
                }
            }
            if(myCrewState.value.data.isEmpty() && !myCrewState.value.isLoading){
                item {
                    Spacer(modifier = Modifier.height(7.dp))
                    EmptyActivityLayout(
                        modifier= Modifier
                            .height(169.dp)
                            .fillMaxWidth()
                            .clickable {
                                findNavController().navigate(R.id.homeFragment)
                            },
                        textString = stringResource(id = R.string.look_around_crew)
                    )
                }
            }else if(myCrewState.value.data.isNotEmpty()) {
                itemsIndexed(
                    items = myCrewState.value.data
                ){ _, item ->
                    MyCrewCard(crew = item){ tabId ->
                        val bundle = Bundle().apply { putInt("crew_id", tabId) }
                        activity?.findNavController(R.id.main_nav_container_view)
                            ?.navigate(R.id.action_mainFragment_to_myCrewDetailFragment, bundle)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = stringResource(id = R.string.arranged_impromptu),
                    fontFamily = Roboto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(myImprtState.value.isLoading){
                item {
                    Box(modifier = Modifier.height(160.dp).fillMaxWidth(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(
                            color = colorResource(id = R.color.main_orange)
                        )
                    }
                }
            }

            if(myImprtState.value.data.isEmpty() && !myImprtState.value.isLoading){
                item {
                    Spacer(modifier = Modifier.height(7.dp))
                    EmptyActivityLayout(
                        modifier= Modifier
                            .height(144.dp)
                            .fillMaxWidth()
                            .clickable {
                                findNavController().navigate(R.id.impromptuFragment)
                            },
                        textString = stringResource(id = R.string.look_around_impromptu)
                    )
                }
            }else if(myImprtState.value.data.isNotEmpty()) {
                itemsIndexed(
                    items = myImprtState.value.data
                ){ _, item ->
                    ImpromptuCard(cardInfo = item) {
                        val bundle = Bundle().apply { putInt("impromptu_id", item.id) }
                        activity?.findNavController(R.id.main_nav_container_view)
                            ?.navigate(R.id.action_mainFragment_to_myImpromptuDetailFragment, bundle)
                    }
                }
            }
        }
    }

    @Composable
    fun EmptyActivityLayout(
        modifier: Modifier, 
        textString : String
    ) {
        val stroke = Stroke(width = 1.dpToPxFloat,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(17f, 17f), 0f)
        )
        
        Box(
            modifier = modifier.padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center)
        {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    cornerRadius = CornerRadius(16.dpToPxFloat, 16.dpToPxFloat),
                    color = Color(ContextCompat.getColor(requireContext(), R.color.main_orange)),
                    style = stroke)
            }
            Text(
                textAlign = TextAlign.Center,
                text = textString,
                fontFamily = Roboto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.main_orange)
            )
        }
    }

    @Composable
    fun MyActivityToolbar(
        modifier: Modifier,
    ) {
        ConstraintLayout(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (icon_notification, text) = createRefs()

                    Text(
                        text = stringResource(id = R.string.my_activity),
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        color = colorResource(id = R.color.black),
                        fontSize = 22.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        modifier = Modifier
                            .constrainAs(icon_notification) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .size(24.dp),
                        onClick = {
                            activity?.findNavController(R.id.main_nav_container_view)
                                ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "search"
                        )
                    }

                }
            }
        }
    }

    val Int.dpToPxFloat: Float
        get() = (this * Resources.getSystem().displayMetrics.density)

}