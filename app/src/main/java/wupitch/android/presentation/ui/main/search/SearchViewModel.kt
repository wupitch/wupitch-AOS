package wupitch.android.presentation.ui.main.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Constants
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val imprtRepository: ImprtRepository
): ViewModel() {

    private var _searchKeyword = mutableStateOf<String>("")
    val searchKeyword : State<String> = _searchKeyword


    /*
    * search
    * */
    val loading = mutableStateOf(false)
    val error = mutableStateOf("")

    fun performSearch(selectedTab : Int, keyword : String) {
        _searchKeyword.value = keyword

        when(selectedTab){
            0 -> searchCrew()
            else -> searchImpromptu()
        }
    }
    /*
    * search crew
    * */

    private var _crewState = mutableStateListOf<CrewCardInfo>()
    val crewState : SnapshotStateList<CrewCardInfo> = _crewState

    private fun searchCrew() = viewModelScope.launch {
        loading.value = true
        val response = crewRepository.getCrewSearch(_searchKeyword.value, _crewPage.value)

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

    /*
    * search impromptu
    * */

    private var _imprtState = mutableStateListOf<ImpromptuCardInfo>()
    val imprtState : SnapshotStateList<ImpromptuCardInfo> = _imprtState

    private fun searchImpromptu() = viewModelScope.launch {
        loading.value = true
        val response = imprtRepository.getSearchImprt( _searchKeyword.value, _imprtPage.value)

        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    if (res.result.first) _imprtState.clear()
                    appendImprtList(res.result.content.map {
                        ImpromptuCardInfo(
                            id = it.impromptuId,
                            remainingDays = it.dday,
                            title = it.title,
                            isPinned = it.isPinUp,
                            time = "${dateDashToCol(it.date)} ${it.day} ${doubleToTime(it.startTime)}",
                            detailAddress = it.location ?: "장소 미정",
                            imprtImage = it.impromptuImage,
                            gatheredPeople = it.nowMemberCount,
                            totalCount = it.recruitmentCount
                        )
                    })
                } else {
                    error.value = res.message
                }
            }
        } else error.value = "번개 조회에 실패했습니다."
        loading.value = false
    }

    /*
    * crew pagination
    * */


    private var _crewPage = mutableStateOf(1)
    val crewPage : State<Int> = _crewPage

    private var crewScrollPosition = 0

    private fun appendCrewList(list: List<CrewCardInfo>) {
        _crewState.addAll(list)
    }

    fun getCrewNewPage() = viewModelScope.launch {
        if ((crewScrollPosition + 1) >= (crewPage.value * Constants.PAGE_SIZE)) {
            incrementCrewPage()

            if (crewPage.value > 1) {
                searchCrew()
            }
        }
    }

    private fun incrementCrewPage() {
        _crewPage.value = _crewPage.value + 1
    }

    fun onChangeCrewScrollPosition(position: Int) {
        crewScrollPosition = position
    }

    /*
   * impromptu pagination
   * */


    private var _imprtPage = mutableStateOf(1)
    val imprtPage : State<Int> = _imprtPage

    private var imprtScrollPosition = 0

    private fun appendImprtList(list: List<ImpromptuCardInfo>) {
        _imprtState.addAll(list)
    }

    fun getImprtNewPage() = viewModelScope.launch {
        if ((imprtScrollPosition + 1) >= (imprtPage.value * Constants.PAGE_SIZE)) {
            incrementImprtPage()

            if (imprtPage.value > 1) {
                searchImpromptu()
            }
        }
    }

    private fun incrementImprtPage() {
        _imprtPage.value = _imprtPage.value + 1
    }

    fun onChangeImprtScrollPosition(position: Int) {
        imprtScrollPosition = position
    }
}