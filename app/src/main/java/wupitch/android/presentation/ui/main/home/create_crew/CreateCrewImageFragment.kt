package wupitch.android.presentation.ui.main.home.create_crew

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Constants.EMPTY_IMAGE_URI
import wupitch.android.common.Constants.INTRO_MAX_LENGTH
import wupitch.android.common.Constants.SUPPLY_MAX_LENGTH
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.util.Sport
import android.provider.MediaStore


@ExperimentalPermissionsApi
@ExperimentalPagerApi
@AndroidEntryPoint
class CreateCrewImageFragment : Fragment() {

    private lateinit var uploadImageBottomSheet: UploadImageBottomSheetFragment
    private val viewModel: CreateCrewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WupitchTheme {

                    val scrollState = rememberScrollState(0)

                    val titleTextState = remember { mutableStateOf(viewModel.crewTitle.value) }
                    val introTextState = remember { mutableStateOf(viewModel.crewIntro.value) }
                    val supplyTextState = remember { mutableStateOf(viewModel.crewSupplies.value) }
                    val inquiryTextState = remember { mutableStateOf(viewModel.crewInquiry.value) }

                    val stopSignupState = remember { mutableStateOf(false) }
                    val dialogOpenState = remember { mutableStateOf(false) }
                    if (stopSignupState.value) {
                        findNavController().navigate(R.id.action_createCrewImageFragment_to_mainFragment)
                    }
                    if (dialogOpenState.value) {
                        StopWarningDialog(
                            dialogOpenState = dialogOpenState,
                            stopSignupState = stopSignupState,
                            textString = stringResource(id = R.string.stop_create_crew_warning)
                        )
                    }
                    val imageChosenState = remember { viewModel.imageChosenState}
                    val isUsingDefaultImage = remember { viewModel.isUsingDefaultImage }
                    val imageUri = remember { viewModel.crewImage }

                    ConstraintLayout(
                        Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        val (toolbar, topDivider, content, bottomDivider, nextBtn) = createRefs()

                        FullToolBar(modifier = Modifier.constrainAs(toolbar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, onLeftIconClick = { findNavController().navigateUp() },
                            onRightIconClick = { dialogOpenState.value = true },
                            textString = R.string.create_crew
                        )

                        Divider(
                            modifier = Modifier
                                .constrainAs(topDivider) {
                                    top.linkTo(toolbar.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

                        Column(modifier = Modifier
                            .constrainAs(content) {
                                top.linkTo(topDivider.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomDivider.top)
                                height = Dimension.fillToConstraints
                                width = Dimension.fillToConstraints
                            }
                            .verticalScroll(scrollState)
                            .padding(horizontal = 20.dp))
                        {
                            IntroImageLayout(imageChosenState, isUsingDefaultImage, imageUri)

                            IntroTextLayout(titleTextState, introTextState)

                            Spacer(modifier = Modifier.height(34.dp))
                            SupplyLayout(supplyTextState)

                            Spacer(modifier = Modifier.height(12.dp))
                            InquiryLayout(inquiryTextState)

                            Spacer(modifier = Modifier.height(40.dp))

                        }

                        Divider(
                            modifier = Modifier
                                .constrainAs(bottomDivider) {
                                    bottom.linkTo(nextBtn.top, margin = 12.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .height(1.dp)
                                .background(colorResource(id = R.color.gray01))
                        )

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
                            btnColor = if(imageChosenState.value && titleTextState.value != "" && introTextState.value != "" && inquiryTextState.value != "" )
                                R.color.main_orange else R.color.gray03,
                            textString = R.string.five_over_seven,
                            fontSize = 16.sp
                        ) {
                            if(imageChosenState.value && titleTextState.value != "" && introTextState.value != "" && inquiryTextState.value != "" ){
                                viewModel.apply {
                                    setCrewTitle(titleTextState.value)
                                    setCrewIntro(introTextState.value)
                                    setCrewSupplies(supplyTextState.value)
                                    setCrewInquiry(inquiryTextState.value)
                                    setImageChosenState(imageChosenState.value)
                                }

                                findNavController().navigate(R.id.action_createCrewImageFragment_to_createCrewFeeFragment)
                            }

                        }
                    }
                }
            }
        }
    }

    private fun openUploadImageBottomSheet() {
        uploadImageBottomSheet = UploadImageBottomSheetFragment(viewModel)
        uploadImageBottomSheet.show(childFragmentManager, "upload image bottom sheet")
    }

    @Composable
    fun InquiryLayout(inquiryTextState: MutableState<String>) {
        Text(
            text = stringResource(id = R.string.create_inquiry),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeTextFieldWithCounter(
            textState = inquiryTextState,
            hintText = stringResource(id = R.string.inquiry_hint),
            maxLength = SUPPLY_MAX_LENGTH
        )

    }

    @Composable
    fun SupplyLayout(
        supplyTextState: MutableState<String>
    ) {
        Text(
            text = stringResource(id = R.string.create_supply),
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.main_black),
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeTextFieldWithCounter(
            textState = supplyTextState,
            hintText = stringResource(id = R.string.input_supplies),
            maxLength = SUPPLY_MAX_LENGTH
        )

    }


    @Composable
    fun IntroTextLayout(
        titleTextState: MutableState<String>,
        introTextState: MutableState<String>,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        SimpleTextField(
            textState = titleTextState,
            hintText = stringResource(id = R.string.input_title),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions.Default
        )
        Spacer(modifier = Modifier.height(24.dp))
        LargeTextFieldWithCounter(
            textState = introTextState,
            hintText = stringResource(id = R.string.input_introduce_crew),
            maxLength = INTRO_MAX_LENGTH
        )

    }

    @Composable
    fun IntroImageLayout(
        imageChosenState : State<Boolean>,
        isUsingDefaultImage : State<Boolean?>,
        imageUri : State<Uri>
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(189.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = R.color.gray04))
                .clickable {
                    openUploadImageBottomSheet()
                },
            contentAlignment = Alignment.Center
        ) {

            if (!imageChosenState.value) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_btn_06_add),
                        contentDescription = "upload_picture_icon",
                        tint = colorResource(id = R.color.gray02)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.representation_image),
                        color = colorResource(id = R.color.gray03),
                        fontSize = 14.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal
                    )
                }
            }else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = if(imageUri.value != EMPTY_IMAGE_URI) rememberImagePainter(data = imageUri.value)
                    else painterResource(id = Sport.getNumOf(viewModel.crewSportId.value).detailImage),
                    contentDescription = "crew image"
                )
            }

            if (isUsingDefaultImage.value == false) {
                GallerySelect(
                    onImageUri = { uri ->
                        if(uri != EMPTY_IMAGE_URI){
                            viewModel.setCrewImage(uri)
                            viewModel.setImageChosenState(true)
                        }
                        viewModel.setIsUsingDefaultImage(null)
                        Log.d("{CreateCrewImageFragment.IntroImageLayout}", uri.toString())
                    }
                )
            }
        }
    }

    fun getRealPathFromURIForGallery(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor: Cursor? = requireActivity().contentResolver.query(
            uri, projection, null,
            null, null
        )
        if (cursor != null) {
            val columnIdx: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            cursor.moveToFirst()
            return cursor.getString(columnIdx)
        }
        assert(false)
        cursor?.close()
        return uri.path
    }

}

