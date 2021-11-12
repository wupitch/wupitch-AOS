package wupitch.android.presentation.ui.main.search.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme

class SearchFragment2 : Fragment() {

    private val tabs = listOf<TabItem>(
        TabItem.Crew,
        TabItem.Impromptu
    )

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


                    Column(Modifier.fillMaxSize()) {

                        val textState = remember {
                            mutableStateOf("")
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(horizontal = 20.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    )
                                    { findNavController().navigateUp() },
                                painter = painterResource(id = R.drawable.left),
                                contentDescription = "go back previous page"
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            SearchTextField(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterVertically), textState = textState
                            )
                        }
                        SearchPager(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 18.dp), scope
                        )
                    }

                }
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun SearchTextField (
        modifier: Modifier,
        textState: MutableState<String>
    ) {
        val focusState = remember {
            mutableStateOf(false)
        }
        val focusRequester = FocusRequester()

        BasicTextField(
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    focusState.value = it.isFocused
                },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                //todo search !!
                Log.d("{SearchFragment.SearchTextField}", "search  ${textState.value}")
            }),
            value = textState.value,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            onValueChange = { value ->
                textState.value = value
            },
            cursorBrush = SolidColor(colorResource(id = R.color.gray03)),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.gray04))
                        .padding(start = 13.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    ConstraintLayout(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .weight(0.9f)
                            .background(Color.Transparent)
                    ) {
                        val (hint) = createRefs()

                        if (focusState.value) {
                            innerTextField()
                        } else if (!focusState.value && textState.value.isEmpty()) {
                            if (textState.value.isEmpty()) {
                                Text(
                                    modifier = Modifier.constrainAs(hint) {
                                        start.linkTo(parent.start)
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                    },
                                    text = stringResource(id = R.string.search),
                                    color = colorResource(
                                        id = R.color.gray03
                                    ),
                                    fontSize = 16.sp,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                    if (textState.value.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .weight(0.1f)
                                .size(24.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                )
                                {
                                    textState.value = ""
//                                    focusState.value = false
                                    focusRequester.freeFocus()
                                },
                            painter = painterResource(id = R.drawable.ic_search_close),
                            contentDescription = "delete search text",
                            tint = colorResource(id = R.color.gray03)
                        )
                    }

                }
            }
        )
    }

    @ExperimentalPagerApi
    @Composable
    fun SearchPager(
        modifier: Modifier,
        scope: CoroutineScope
    ) {
        val pagerState = rememberPagerState()
        Scaffold(
            modifier = modifier,
            content = {
                TabPage(pagerState = pagerState, tabItems = tabs)
            },
            topBar = {
                SearchTabRow(
                    tabs = tabs,
                    selectedIndex = pagerState.currentPage,
                    onPageSelected = { tabItem ->
                        scope.launch {
                            pagerState.scrollToPage(tabItem.index)
                        }
                    }
                )
            }
        )
    }

    @Composable
    fun SearchTabRow(
        tabs: List<TabItem>,
        selectedIndex: Int,
        onPageSelected: ((tabItem: TabItem) -> Unit)
    ) {
        TabRow(selectedTabIndex = selectedIndex,
            divider = {
                Divider(
                    Modifier
                        .height(1.dp)
                        .background(colorResource(id = R.color.gray01))
                )
            }) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(
                    modifier = Modifier.background(Color.White),
                    selected = index == selectedIndex,
                    onClick = {
                        onPageSelected(tabItem)
                    },
                    text = {
                        Text(
                            text = stringResource(id = tabItem.title),
                            color = Color.Black,
                            fontFamily = Roboto,
//                        fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }
                )
            }

        }
    }

    @ExperimentalPagerApi
    @Composable
    fun TabPage(pagerState: PagerState, tabItems: List<TabItem>) {
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
        ) { index ->
            tabItems[index].screenToLoad()
        }
    }
}

sealed class TabItem(
    val index: Int,
    @StringRes val title: Int,
    val screenToLoad: @Composable () -> Unit
) {
    object Crew : TabItem(0, R.string.crew, {
        CrewSearchTab()
    })

    object Impromptu : TabItem(1, R.string.impromptu, {
        ImpromptuSearchTab()
    })
}