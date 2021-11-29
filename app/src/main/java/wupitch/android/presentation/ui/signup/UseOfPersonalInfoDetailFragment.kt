package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.FullToolBar
import wupitch.android.presentation.ui.components.IconToolBar
import wupitch.android.presentation.ui.components.TitleToolbar

class UseOfPersonalInfoDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val scrollState = rememberScrollState(0)

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        val (toolbar, divider, content) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { findNavController().navigateUp() },
                            textString = R.string.terms_of_privacy_policy
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(divider) {
                                    top.linkTo(toolbar.bottom)
                                }
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    colorResource(
                                        id = R.color.gray01
                                    )
                                )
                        )

                        TextContent(
                            modifier = Modifier
                                .constrainAs(content) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(divider.bottom, margin = 24.dp)
                                    bottom.linkTo(parent.bottom)
                                },
                            scrollState = scrollState
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TextContent(
        modifier: Modifier,
        scrollState: ScrollState
    ) {
        Column(modifier = modifier.verticalScroll(scrollState)) {
//            Text(text = stringResource(id = R.string.privacy_policy))
        }

    }
}