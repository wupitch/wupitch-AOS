package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*

@AndroidEntryPoint
class MyPageSportFragment : Fragment() {


    private val viewModel: MyPageViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val userSportListState = remember { viewModel.userSportList }

                    val sportsList = viewModel.sportsList.value

                    val updateState = remember { viewModel.updateState }
                    if (updateState.value.isSuccess) findNavController().navigateUp()
                    if (updateState.value.error.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            updateState.value.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, content, nextBtn, progressBar) = createRefs()

                        IconToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            findNavController().navigateUp()
                        })

                        if (sportsList.isLoading) {
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

                        if (sportsList.data.isNotEmpty()) {
                            val sportRememberList = mutableListOf<FilterItem>()

                            sportsList.data.forEach {
                                if (it.state.value) {
                                    sportRememberList.add(FilterItem(it.name, remember {
                                        mutableStateOf(true)
                                    }))
                                } else {
                                    sportRememberList.add(FilterItem(it.name, remember {
                                        mutableStateOf(false)
                                    }))
                                }
                            }

                            Column(Modifier.constrainAs(content) {
                                top.linkTo(toolbar.bottom, margin = 24.dp)
                                start.linkTo(parent.start, margin = 20.dp)
                            }) {
                                Row(verticalAlignment = Alignment.Bottom) {

                                    Text(
                                    text = stringResource(id = R.string.select_sport),
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.main_black),
                                    fontSize = 22.sp,
                                    lineHeight = 32.sp
                                )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        modifier = Modifier.padding(top = 4.dp),
                                        text = stringResource(id = R.string.select_sport_at_least_one),
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.Normal,
                                        color = colorResource(id = R.color.gray02),
                                        fontSize = 12.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                RepetitionLayout(
                                    text = null,
                                    filterItemList = sportRememberList.toList(),
                                    modifier = Modifier
                                        .width(152.dp)
                                        .height(48.dp),
                                    checkedListState = userSportListState
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
                            btnColor = if (userSportListState.isNotEmpty()) R.color.main_orange
                            else R.color.gray03,
                            textString = R.string.done,
                            fontSize = 16.sp
                        ) {
                            if (userSportListState.isNotEmpty()) {
                                viewModel.setUserSportList(userSportListState)
                                viewModel.changeUserSport()
                            }
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