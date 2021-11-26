package wupitch.android.presentation.ui.main.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.TitleToolbar
import wupitch.android.presentation.ui.main.my_page.components.MyPageText

class MyPageInfoFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {
                    Column(Modifier.fillMaxSize().background(Color.White)) {

                        TitleToolbar(
                            modifier = Modifier.fillMaxWidth(),
                            textString = R.string.profile
                        ) {
                            findNavController().navigateUp()
                        }

                        MyPageText(
                            modifier = Modifier.padding(start = 20.dp),
                            textString = stringResource(id = R.string.nickname_intro)
                        ){
                            findNavController().navigate(R.id.action_myPageInfoFragment_to_myPageProfileFragment)
                        }
                        MyPageText(
                            modifier = Modifier.padding(start = 20.dp),
                            textString = stringResource(id = R.string.profile_district)
                        ){
                            findNavController().navigate(R.id.action_myPageInfoFragment_to_regionFragment)
                        }
                        MyPageText(
                            modifier = Modifier.padding(start = 20.dp),
                            textString = stringResource(id = R.string.profile_interested_sport)
                        ){
                            findNavController().navigate(R.id.action_myPageInfoFragment_to_sportFragment)
                        }
                        MyPageText(
                            modifier = Modifier.padding(start = 20.dp),
                            textString = stringResource(id = R.string.profile_agegroup)
                        ){
                            findNavController().navigate(R.id.action_myPageInfoFragment_to_myPageAgeGroupFragment)
                        }
                        MyPageText(
                            modifier = Modifier.padding(start = 20.dp),
                            textString = stringResource(id = R.string.profile_contact)
                        ){
                            findNavController().navigate(R.id.action_myPageInfoFragment_to_myPageContactFragment)
                        }



                    }
                }
            }
        }
    }
}