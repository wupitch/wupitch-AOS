package wupitch.android.presentation.ui.main.impromptu

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.presentation.ui.main.impromptu_detail.JoinImpromptuState
import wupitch.android.util.TimeType
import wupitch.android.util.isEndTimeFasterThanStart
import javax.inject.Inject

@HiltViewModel
class ImpromptuViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository
) : ViewModel() {

    private var _impromptuListState = mutableStateOf(ImpromptuListState())
    val impromptuListState : State<ImpromptuListState> = _impromptuListState

    val impromptuList : MutableState<List<ImpromptuCardInfo>> = mutableStateOf(
        listOf<ImpromptuCardInfo>(
            ImpromptuCardInfo(
                1,
                11,
                true,
                "가나다라마바사아자차...",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                10,
                12,
                false,
                "번개합시다",
                false,
                "월요일 23:00 - 24:00",
                false,
                "동백 2로 37",
                1,
                3
            ),
            ImpromptuCardInfo(
                1,
                13,
                true,
                "날씨좋다 모이자",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                14,
                true,
                "천둥번개!",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                15,
                true,
                "번개 제목",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37",
                2,
                3
            ),
            ImpromptuCardInfo(
                1,
                16,
                true,
                "마지막 번개",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37",
                2,
                3
            ),
        )
    )

    private var _district = MutableLiveData<Resource<Array<String>>>()
    val district : LiveData<Resource<Array<String>>> = _district

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    private var _joinImpromptuState = mutableStateOf(JoinImpromptuState())
    val joinImpromptuState : State<JoinImpromptuState> = _joinImpromptuState


    //todo get list from server.
    fun getImpromptuList () = viewModelScope.launch {
        _impromptuListState.value = ImpromptuListState(isLoading = true)

        delay(500L)

        _impromptuListState.value = ImpromptuListState(data = impromptuList.value)
    }

    fun getDistricts () = viewModelScope.launch {
        _district.value = Resource.Loading<Array<String>>()

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let { districtRes ->
                if(districtRes.isSuccess) _district.value = Resource.Success<Array<String>>(districtRes.result.map { it.name }.toTypedArray())
                else _district.value = Resource.Error<Array<String>>(null, "지역 가져오기를 실패했습니다.")
            }
        } else _district.value = Resource.Error<Array<String>>(null, "지역 가져오기를 실패했습니다.")

    }

    fun initJoinImpromptuState() {
        _joinImpromptuState.value = JoinImpromptuState()
    }

    fun joinImpromptu() = viewModelScope.launch {
        _joinImpromptuState.value = JoinImpromptuState(isLoading = true)
        delay(1200L)
        //todo
        _joinImpromptuState.value = JoinImpromptuState(isSuccess = true)
    }

}