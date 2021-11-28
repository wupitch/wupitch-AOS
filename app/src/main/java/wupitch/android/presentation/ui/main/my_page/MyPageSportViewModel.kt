package wupitch.android.presentation.ui.main.my_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.domain.repository.ProfileRepository
import wupitch.android.presentation.ui.main.home.create_crew.SportState
import javax.inject.Inject

@HiltViewModel
class MyPageSportViewModel @Inject constructor(
    private val getSportRepository: GetSportRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    //sports
    private var _userSportList = mutableStateListOf<Int>()
    val userSportList: SnapshotStateList<Int> = _userSportList

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _userSportId = mutableStateOf(-1)
    val userSportId: State<Int> = _userSportId

    fun getUserSport() = viewModelScope.launch {
        val response = profileRepository.getUserSports()


    }


    //todo sport without etc and edit this.
    fun getSports() = viewModelScope.launch {
        _sportsList.value = SportState(isLoading = true)

        val response = getSportRepository.getSport()
        if (response.isSuccessful) {
            response.body()?.let { sportRes ->
                if (sportRes.isSuccess) {
                    _sportsList.value =
                        SportState(data = sportRes.result.filter { it.sportsId < sportRes.result.size }
                            .map { it.toFilterItem() })
                    _sportsList.value.data.forEachIndexed { index, filterItem ->
                        if (_userSportId.value == index) {
                            filterItem.state.value = true
                        }
                    }
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }

    fun setUserSportList(list: SnapshotStateList<Int>) {
        _userSportList = list
    }
    private var _updateState = mutableStateOf(BaseState())
    val updateState : State<BaseState> = _updateState

    //todo
    fun changeUserSport() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            sportsList = _userSportList.map { it +1 }//if(_userDistrictId.value == _userInfo.value.data.) null else _userDistrictId.value + 1,
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!
    }
}