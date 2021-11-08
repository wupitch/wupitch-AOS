package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.SetToolBar

class UseOfPersonalInfoDetailFragment : Fragment() {
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
                        val (toolbar, divider) = createRefs()

                        SetToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, onLeftIconClick = {
                            findNavController().navigateUp()
                        }, textString = R.string.terms_of_privacy_policy,
                            hasRightIcon = true,
                            onRightIconClick = {
                                findNavController().navigateUp()
                            }
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
                    }

                }

            }
        }
    }
}