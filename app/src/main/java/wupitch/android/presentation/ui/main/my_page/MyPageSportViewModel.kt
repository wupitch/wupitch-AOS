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

    private var _updateState = mutableStateOf(BaseState())
    val updateState: State<BaseState> = _updateState

    private var _userSportList = mutableStateListOf<Int>()
    val userSportList: SnapshotStateList<Int> = _userSportList


    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    var initList = listOf<Int>()


    private fun getUserSport() = viewModelScope.launch {
        val response = profileRepository.getUserSports()

        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    res.result.list?.forEach { _userSportList.add(it.sportsId - 1) }
                    initList = _userSportList.toList()
                    res.result.list?.forEachIndexed { index, item ->
                        _sportsList.value.data[item.sportsId - 1].state.value = true
                    }
                } else _updateState.value = BaseState(error = res.message)
            }
        } else _updateState.value = BaseState(error = "관심 스포츠 조회에 실패했습니다.")
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
                    getUserSport()
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }

    fun setUserSportList(list: SnapshotStateList<Int>) {
        _userSportList = list
    }


    fun changeUserSport() = viewModelScope.launch {
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            sportsList = _userSportList.map { it + 1 }
        )
        val response = profileRepository.updateUserInfo(req)
        if (response.isSuccessful) {
            response.body()?.let {
                if (it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        } else _updateState.value = BaseState(error = "정보 변경에 실패했습니다.")
    }
}