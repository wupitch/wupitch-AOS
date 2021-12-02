package wupitch.android.presentation.ui.main.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.Constants
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val crewRepository: CrewRepository
): ViewModel() {

    private var _searchKeyword = mutableStateOf<String>("")
    val searchKeyword : State<String> = _searchKeyword


    private var _impromptuSearchState = mutableStateOf(ImpromptuSearchState())
    val impromptuSearchState : State<ImpromptuSearchState> = _impromptuSearchState

    private var districtId : Int? = null

    fun setDistrictId(id : Int?) {
        districtId = id
    }


    fun performSearch(selectedTab : Int, keyword : String) {
        _searchKeyword.value = keyword
        Log.d("{SearchViewModel.performSearch}", "category : $selectedTab keyword : $keyword")

        when(selectedTab){
            0 -> searchCrew()
            else -> searchImpromptu()
        }
    }

    private var _crewState = mutableStateListOf<CrewCardInfo>()
    val crewState : SnapshotStateList<CrewCardInfo> = _crewState

    private fun searchCrew() = viewModelScope.launch {
        loading.value = true
        val response = crewRepository.getCrewSearch(if(districtId==null) null else districtId!!+1, _searchKeyword.value, _page.value)

        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess){
                    if(res.result.first) _crewState.clear()
                    appendCrewList(res.result.content.map {
                        CrewCardInfo(
                            id = it.clubId,
                            sportId = it.sportsId -1,
                            isPinned = it.isPinUp,
                            time = "${it.schedules[0].day} ${doubleToTime(it.schedules[0].startTime)}-${doubleToTime(it.schedules[0].endTime)}",
                            isMoreThanOnceAWeek = it.schedules.size >1,
                            detailAddress = it.areaName ?: "장소 미정",
                            crewImage = it.crewImage,
                            title = it.clubTitle
                        )})
                } else {
                    error.value = res.message
                }
            }
        }else error.value = "크루 조회에 실패했습니다."
        loading.value = false

    }

    private fun searchImpromptu() = viewModelScope.launch {
        _impromptuSearchState.value = ImpromptuSearchState(isLoading = true)
        delay(1200L)
        _impromptuSearchState.value = ImpromptuSearchState()

    }

    /*
    * pagination
    * */

    val loading = mutableStateOf(false)
    val error = mutableStateOf("")

    private var _page = mutableStateOf(1)
    val page : State<Int> = _page

    private var scrollPosition = 0

    private fun appendCrewList(list: List<CrewCardInfo>) {
        _crewState.addAll(list)
    }

    fun getNewPage(tabId : Int) = viewModelScope.launch {
        if ((scrollPosition + 1) >= (page.value * Constants.PAGE_SIZE)) {
            incrementPage()
            Log.d("{HomeViewModel.newPage}", "${page.value}")

            if (page.value > 1) {
                if(tabId == 0) searchCrew()
                else searchImpromptu()
            }
        }
    }

    private fun incrementPage() {
        _page.value = _page.value + 1
    }

    fun onChangeScrollPosition(position: Int) {
        scrollPosition = position
    }
}