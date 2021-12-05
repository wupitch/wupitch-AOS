package wupitch.android.presentation.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.remember
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.main.home.components.CrewCard
import wupitch.android.util.checkKeywordLen

@AndroidEntryPoint
class SearchCrewFragment : Fragment() {

    private val viewModel : SearchViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val searchState = remember { viewModel.crewState }
                    val searchKeyword = viewModel.searchKeyword.value

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (progressbar, text, chrt, crewList) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

                        if (searchState.isNotEmpty()) {
                            Box(modifier = Modifier
                                .constrainAs(crewList) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                    height = Dimension.fillToConstraints
                                }
                                .background(Color.White)) {

                                LazyColumn {
                                    item {
                                        Spacer(modifier = Modifier.height(24.dp))
                                    }
                                    itemsIndexed(
                                        items = searchState
                                    ) { index, crew ->
                                        viewModel.onChangeCrewScrollPosition(index)
                                        if((index +1) >= (viewModel.crewPage.value * Constants.PAGE_SIZE) && !viewModel.loading.value){
                                            viewModel.getCrewNewPage()
                                        }
                                        CrewCard(crewCard = crew, onClick = {
                                            val bundle = Bundle().apply { putInt("crewId", crew.id) }
                                            findNavController().navigate(R.id.action_searchFragment_to_crewDetailFragment, bundle)
                                        })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(22.dp))
                                    }
                                }
                            }

                        } else {
                            if(searchKeyword.isNotEmpty() && !viewModel.loading.value) {

                                Image(
                                    modifier = Modifier
                                        .constrainAs(chrt) {
                                            bottom.linkTo(text.top)
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                        }
                                        .size(130.dp, 210.dp),
                                    painter = painterResource(id = R.drawable.img_chrt_02),
                                    contentDescription = null
                                )
                                Text(
                                    modifier = Modifier
                                        .constrainAs(text) {
                                            start.linkTo(parent.start)
                                            end.linkTo(parent.end)
                                            bottom.linkTo(guildLine)
                                        }
                                        .padding(top = 24.dp),
                                    text = stringResource(R.string.no_search_result, checkKeywordLen(searchKeyword)),
                                    color = colorResource(id = R.color.gray02),
                                    fontFamily = Roboto,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                        }

                        if (viewModel.loading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(progressbar) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                                color = colorResource(id = R.color.main_orange)
                            )
                        }
                    }

                }
            }
        }
    }
}

