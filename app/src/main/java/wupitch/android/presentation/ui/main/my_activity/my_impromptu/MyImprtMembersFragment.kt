package wupitch.android.presentation.ui.main.my_activity.my_impromptu

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import wupitch.android.R
import wupitch.android.domain.model.CrewMember
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.main.my_activity.components.CrewMemberLayout

class MyImprtMembersFragment : Fragment() {

    private val viewModel : MyImpromptuViewModel by viewModels({requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMembers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    val memberState = remember {viewModel.memberState}

                    ConstraintLayout(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)) {
                        val (chrt, text, members, progressbar) = createRefs()
                        val guildLine = createGuidelineFromTop(0.65f)

                        if(memberState.value.data.isNotEmpty()){
                            LazyColumn(modifier = Modifier.constrainAs(members){
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            }) {
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                itemsIndexed(items = memberState.value.data){ _, item ->
//                                    CrewMemberLayout(member = item){ //todo 버튼 추가시 변경.
//                                        val bundle = Bundle().apply { putInt("memberId", it) }
//                                        findNavController().navigate(R.id.action_myImpromptuDetailFragment_to_memberDetailFragment, bundle)
//
//                                    }
                                }
                            }
                        }

                        if(memberState.value.isLoading){
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