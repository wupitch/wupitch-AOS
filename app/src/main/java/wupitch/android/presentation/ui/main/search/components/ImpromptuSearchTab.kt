package wupitch.android.presentation.ui.main.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.main.search.SearchViewModel

@Composable
fun ImpromptuSearchTab(
    viewModel: SearchViewModel = hiltViewModel()
) {
    //todo 현재는 crew search 와 연결되어 있음
    val searchState = remember {viewModel.crewState}
    val searchKeyword = viewModel.searchKeyword.value

    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (progressbar, text, chrt) = createRefs()
        val guildLine = createGuidelineFromTop(0.65f)

        if (searchState.isNotEmpty()) {
            //todo show list.

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
                    modifier = Modifier.constrainAs(text) {
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