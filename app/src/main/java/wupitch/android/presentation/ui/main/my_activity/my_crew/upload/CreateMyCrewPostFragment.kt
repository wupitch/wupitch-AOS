package wupitch.android.presentation.ui.main.my_activity.my_crew.upload

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants
import wupitch.android.common.Constants.ANNOUNCE_TITLE_LENGTH
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.my_activity.my_crew.MyCrewViewModel

@AndroidEntryPoint
class CreateMyCrewPostFragment : Fragment() {

    private val viewModel : UploadToMyCrewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getInt("crewId")?.let { viewModel.setCrewId(it) }
    }

    @ExperimentalPagerApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val uploadState = remember {viewModel.uploadPostState}
                    if(uploadState.value.isSuccess){
                        findNavController().navigateUp()
                    }

                    val contentTextState = remember { mutableStateOf("")}
                    val announceToggleState = remember { mutableStateOf(false)}
                    val announceTitleState = remember {mutableStateOf("")}
                    
                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, divider, content, nextBtn, progressBar) = createRefs()

                        TitleToolbar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, textString = R.string.crew) {
                                findNavController().navigateUp()
                        }

                        Divider(
                            Modifier
                                .constrainAs(divider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                                .width(1.dp)
                                .background(colorResource(id = R.color.gray01)))

                        Column(modifier = Modifier.constrainAs(content){
                            start.linkTo(parent.start, margin = 20.dp)
                            end.linkTo(parent.end, margin = 20.dp)
                            width = Dimension.fillToConstraints
                            top.linkTo(divider.bottom, margin = 24.dp)
                        }) {

                            LargeTextFieldWithCounter(
                                textState = contentTextState,
                                hintText = stringResource(id = R.string.input_content),
                                maxLength = Constants.SUPPLY_MAX_LENGTH,
                                onFocusChanged = {}
                            )


                            NoToggleLayout(announceToggleState, stringResource(id = R.string.announcement_post))
                            Spacer(modifier = Modifier.height(20.dp))

                            if(announceToggleState.value){

                                SimpleTextField(
                                    textState = announceTitleState,
                                    hintText = stringResource(id = R.string.input_announcement),
                                    maxLength = ANNOUNCE_TITLE_LENGTH,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        setKeyboardDown()
                                    }),
                                    onFocusChanged = {}
                                )
                            }



                        }

                        if (uploadState.value.isLoading) {
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
                            btnColor = if(checkValid(contentTextState, announceToggleState, announceTitleState)) R.color.main_orange
                            else R.color.gray03,
                            textString = R.string.upload,
                            fontSize = 16.sp
                        ) {
                            if(checkValid(contentTextState, announceToggleState, announceTitleState))
                                viewModel.uploadPost()
                        }
                    }


                }
            }
        }
    }

    private fun checkValid(
        contentState : MutableState<String>,
        announceToggle : MutableState<Boolean>,
        titleState : MutableState<String> ) : Boolean{
        return if(announceToggle.value){
            contentState.value.isNotEmpty() && titleState.value.isNotEmpty()
        }else {
            contentState.value.isNotEmpty()
        }
    }

    private fun setKeyboardDown() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}