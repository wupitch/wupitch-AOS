package wupitch.android.presentation.ui.main.my_page

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants.EMPTY_IMAGE_URI
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.GallerySelect
import wupitch.android.presentation.ui.components.GrayDivider
import wupitch.android.presentation.ui.components.NotiToolbar
import wupitch.android.presentation.ui.components.UploadImageBottomSheetFragment
import wupitch.android.presentation.ui.main.my_page.components.FillInfoSnackbar
import wupitch.android.presentation.ui.main.my_page.components.MyPageText

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private val viewModel : MyPageViewModel by viewModels()
    private lateinit var uploadImageBottomSheet: UploadImageBottomSheetFragment


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkNotEnoughInfo()
        viewModel.getUserInfo()
    }

    private fun openUploadImageBottomSheet() {
        uploadImageBottomSheet = UploadImageBottomSheetFragment(viewModel)
        uploadImageBottomSheet.show(childFragmentManager, "upload image bottom sheet")
    }


    @ExperimentalPermissionsApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                val notEnoughInfoState = remember { viewModel.notEnoughInfo}
                val snackbarHostState = remember { SnackbarHostState() }
                val userInfoState = remember {viewModel.userInfo}

                val uploadImageState = remember {viewModel.uploadImageState}
                if(uploadImageState.value.error.isNotEmpty()){
                    Toast.makeText(requireContext(), uploadImageState.value.error, Toast.LENGTH_SHORT).show()
                }

                if(userInfoState.value.error.isNotEmpty()){
                    Toast.makeText(requireContext(), userInfoState.value.error, Toast.LENGTH_SHORT).show()
                }

                if(notEnoughInfoState.value){
                   LaunchedEffect(key1 = snackbarHostState, block = {
                       viewModel.setNotEnoughInfo()
                       snackbarHostState.showSnackbar(
                        message = getString(R.string.fill_info_guide),
                        duration = SnackbarDuration.Short
                    )
                })
                }
                val imageChosenState = remember { viewModel.imageChosenState}
                val isUsingDefaultImage = remember { viewModel.isUsingDefaultImage }
                val imageUri = remember { viewModel.userImageState}


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    NotiToolbar(
                        modifier = Modifier.fillMaxWidth(),
                        textString = stringResource(id = R.string.my_page)
                    ) {
                        activity?.findNavController(R.id.main_nav_container_view)
                            ?.navigate(R.id.action_mainFragment_to_notificationFragment)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        val (profile, alert, myActivity, myRecord, progressbar) = createRefs()


                        if(isUsingDefaultImage.value == false) {
                            GallerySelect(
                                onImageUri = { uri ->
                                    if(uri != EMPTY_IMAGE_URI){
                                        viewModel.uploadUserImage(uri)
                                        viewModel.setImageChosenState(true)
                                    }
                                    viewModel.setIsUsingDefaultImage(null)
                                }
                            )
                        }

                        ProfileBox(
                            modifier = Modifier
                                .constrainAs(profile) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            userInfoState = userInfoState,
                            imageUri = imageUri,
                            imageChosenState = imageChosenState,
                            onImageClick = {
                                openUploadImageBottomSheet()
                            },
                            onDetailClick = {
                                activity?.findNavController(R.id.main_nav_container_view)
                                    ?.navigate(R.id.action_mainFragment_to_myPageInfoFragment)
                            }
                        )

                        MyPageText(
                            modifier = Modifier
                                .constrainAs(myActivity) {
                                    start.linkTo(parent.start)
                                    top.linkTo(profile.bottom, margin = 22.dp)
                                },
                            textString = stringResource(id = R.string.interested_activity)
                        ){
                            Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                        }

                        MyPageText(
                            modifier = Modifier
                                .constrainAs(myRecord) {
                                    start.linkTo(parent.start)
                                    top.linkTo(myActivity.bottom)
                                },
                            textString = stringResource(id = R.string.my_record)
                        ){
                            Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                        }
                        FillInfoSnackbar(
                            modifier = Modifier.constrainAs(alert){
                                end.linkTo(parent.end)
                                top.linkTo(profile.top, margin = 93.dp)
                            },
                            snackbarHostState = snackbarHostState
                        )

                        if (userInfoState.value.isLoading || uploadImageState.value.isLoading) {
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

                    GrayDivider()
                    Spacer(modifier = Modifier.height(4.dp))
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.announcement)
                    ){
                        Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.faq)
                    ){
                        Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.mypage_inquiry)
                    ){
                        Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.settings)
                    ){
                        val bundle = Bundle().apply { putBoolean("isPushAgreed", userInfoState.value.data.isPushAgree) }
                        activity?.findNavController(R.id.main_nav_container_view)
                            ?.navigate(R.id.action_mainFragment_to_myPageSettingFragment, bundle)
                    }
                    MyPageText(
                        modifier = Modifier.padding(start = 20.dp),
                        textString = stringResource(id = R.string.dev_info)
                    ){
                        Toast.makeText(requireContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }




    @Composable
    private fun ProfileBox(
        modifier: Modifier,
        userInfoState : State<UserInfoState>,
        imageChosenState : State<Boolean>,
        imageUri : State<Uri>,
        onImageClick : () -> Unit,
        onDetailClick : () -> Unit
    ) {

        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.gray04))
                .border(
                    shape = RoundedCornerShape(16.dp),
                    width = 2.dp,
                    color = colorResource(id = R.color.gray01)
                )
        ) {
            val (image, camera, text, button) = createRefs()

            if (imageChosenState.value) {
                Image(
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(parent.top, margin = 18.dp)
                            bottom.linkTo(parent.bottom, margin = 18.dp)
                            start.linkTo(parent.start, margin = 24.dp)
                        }.size(64.dp),
                    painter = if(imageUri.value != EMPTY_IMAGE_URI) {
                        rememberImagePainter(
                            imageUri.value,
                            builder = {
                                placeholder(R.color.white)
                                transformations(CircleCropTransformation())
                                build()
                            }
                        )
                    }else { rememberImagePainter(R.drawable.profile_basic) },
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }else {
                 Image(
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(parent.top, margin = 18.dp)
                            bottom.linkTo(parent.bottom, margin = 18.dp)
                            start.linkTo(parent.start, margin = 24.dp)
                        }.size(64.dp),
                    painter = if(userInfoState.value.data.profileImageUrl != null) {
                        rememberImagePainter(
                            userInfoState.value.data.profileImageUrl,
                            builder = {
                                placeholder(R.color.white)
                                transformations(CircleCropTransformation())
                                build()
                            }
                        )
                    }else { rememberImagePainter(R.drawable.profile_basic) },
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(camera) {
                        top.linkTo(image.top, margin = 32.dp)
                        start.linkTo(image.start, margin = 32.dp)
                    },
                onClick = {
                    onImageClick()
                }){
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.btn_photo), contentDescription = null )
                }

            Column(modifier = Modifier.constrainAs(text) {
                top.linkTo(image.top, margin = 8.dp)
                start.linkTo(image.end, margin = 20.dp)
                end.linkTo(button.start, margin = 10.dp)
                width = Dimension.fillToConstraints
            }) {
                Text(
                    text = userInfoState.value.data.nickname,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )

                Text(
                    modifier = Modifier.padding(top=2.dp),
                    text =  userInfoState.value.data.introduce,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.gray02),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .size(24.dp),
                onClick = { onDetailClick() }) {
                Icon(painter = painterResource(id = R.drawable.i_arrows_chevron_backward),
                    contentDescription = null,
                    tint = colorResource(id = R.color.gray05)
                )

            }
        }
    }
}