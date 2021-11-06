package wupitch.android.presentation.ui.main.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.repository.GetDistrictRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository
) : ViewModel() {

    val loading = mutableStateOf(false)
    val crewList : MutableState<List<CrewCardInfo>> = mutableStateOf(
        listOf<CrewCardInfo>(
            CrewCardInfo(
                0,
                "축구",
                "법정동",
                true,
                "크루이름",
                true,
                "월요일 23:00 - 24:00",
                true,
                "동백 2로 37"
            ),
            CrewCardInfo(
                1,
                "농구",
                "이매동",
                false,
                "농구하자고고씽",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                23,
                "농구",
                "이매동",
                false,
                "아무이름이지롱",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                44,
                "농구",
                "이매동",
                false,
                "농구하자히히",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                54,
                "농구",
                "이매동",
                false,
                "농구하자고고씽하하",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                56,
                "농구",
                "이매동",
                false,
                "농구하자고고씽호호",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            ),
            CrewCardInfo(
                77,
                "농구",
                "이매동",
                false,
                "농구하자고고씽ㅇㅇㅇ",
                false,
                "화요일 23:00 - 24:00",
                false,
                "이매동 럭키타운 농구장"
            )
        )
    )

    private var _district = MutableLiveData<Resource<DistrictRes>>()
    val district : LiveData<Resource<DistrictRes>> = _district

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId : LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName : LiveData<String> = _userDistrictName

    fun setUserRegion(districtId : Int, districtName : String) {
        //todo 서버에 보내기
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{HomeViewModel.setUserRegion}", "id : $districtId name : $districtName")
    }

    fun getDistricts () = viewModelScope.launch {
        _district.value = Resource.Loading<DistrictRes>()

        val response = getDistrictRepository.getDistricts()
        if(response.isSuccessful) {
            response.body()?.let {
                if(it.isSuccess) _district.value = Resource.Success<DistrictRes>(it)
                else _district.value = Resource.Error<DistrictRes>(null, "지역 가져오기를 실패했습니다.")
            }
        } else _district.value = Resource.Error<DistrictRes>(null, "지역 가져오기를 실패했습니다.")

    }



}