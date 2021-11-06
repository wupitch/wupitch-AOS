package wupitch.android.presentation.ui.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.MainViewModel
import wupitch.android.presentation.ui.main.home.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DistrictBottomSheetFragment @Inject constructor(
    val viewModel: ViewModel
) : BottomSheetDialogFragment() {


    lateinit var state : Resource<DistrictRes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {


            setContent {

                val pickerValueState = remember {
                    mutableStateOf(0)
                }


                //todo 이거 어떻게 예쁘게 안 될까??
                when(viewModel){
                    is MainViewModel-> {
                        viewModel.district.observeAsState().value?.let {
                            state = it
                        }
                    }
                    is HomeViewModel -> {
                        viewModel.district.observeAsState().value?.let {
                            state = it
                        }
                    }
                }

                state.data?.let { result ->
                    val districtArray  = ArrayList(result.result.map { it.name }).toTypedArray()
                    Log.d("{DistrictBottomSheetFragment.onCreateView}", districtArray.toString())

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(398.dp)
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp)
                        ) {
                            val (title, close, numPicker, btn_select) = createRefs()

                            Text(
                                modifier = Modifier.constrainAs(title) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                                text = stringResource(id = R.string.select_region_bottom_sheet),
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.main_black)
                            )

                            Image(
                                modifier = Modifier
                                    .constrainAs(close) {
                                        top.linkTo(title.top)
                                        end.linkTo(parent.end)
                                        bottom.linkTo(title.bottom)
                                    }
                                    .size(24.dp)
                                    .clickable {
                                        dismiss()
                                    },
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "close icon"
                            )

                            AndroidView(modifier = Modifier
                                .width(108.dp)
                                .constrainAs(numPicker) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(title.bottom)
                                    bottom.linkTo(btn_select.top)
                                },
                                factory = { context ->
                                    NumberPicker(context).apply {
                                        minValue = 0
                                        maxValue = result.result.size -1
                                        displayedValues = districtArray
                                        setOnValueChangedListener { picker, oldVal, newVal ->
                                            //서울시 : 0 에서 바뀌지 않으면 불리지 않음. pickerValueState default : 0
                                            pickerValueState.value =  picker.value
                                        }
                                    }
                                }
                            )

                            Button(
                                modifier = Modifier
                                    .constrainAs(btn_select) {
                                        bottom.linkTo(parent.bottom, margin = 32.dp)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                onClick = {
                                    // todo : viewModel 에 number picker value 보내기. & view model 값 state 로 받아서 crew list 변경.
                                    Log.d("{DistrictBottomSheetFragment.onCreateView}", pickerValueState.value.toString())
                                    when (viewModel) {
                                        is MainViewModel -> {
                                            viewModel.setUserRegion(pickerValueState.value, districtArray[pickerValueState.value])
                                        }
                                        is HomeViewModel -> {
                                            viewModel.setUserRegion(pickerValueState.value, districtArray[pickerValueState.value])
                                        }
                                    }
                                    dismiss()
                                },
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main_black))
                            ) {

                                Text(
                                    text = stringResource(id = R.string.selection),
                                    color = Color.White,
                                    fontFamily = Roboto,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                if(state is Resource.Error<DistrictRes>) {
                    //todo handle error.
                }
                if(state is Resource.Loading<DistrictRes>){
                    //todo handle loading
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (viewModel as MainViewModel).getDistricts()
    }

}