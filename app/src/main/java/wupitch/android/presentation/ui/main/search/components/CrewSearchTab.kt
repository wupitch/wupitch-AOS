package wupitch.android.presentation.ui.main.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.main.search.SearchViewModel

@Composable
fun CrewSearchTab(
    viewModel: SearchViewModel = hiltViewModel()
) {

    val searchState = viewModel.crewSearchState.value
    val searchKeyword = viewModel.searchKeyword.value

    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (progressbar, text, chrt) = createRefs()
        val guildLine = createGuidelineFromTop(0.65f)

        if (searchState.data.isNotEmpty()) {
            //todo show list.

        } else {
            if(searchKeyword.isNotEmpty() && !searchState.isLoading) {

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

        if (searchState.isLoading) {
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

fun checkKeywordLen(searchKeyword: String): String {
    return if(searchKeyword.length > 7) {
        searchKeyword.substring(0, 7) + "..."
    }else searchKeyword
}
