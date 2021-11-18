package wupitch.android.presentation.ui.main.search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModel() {

    private var _searchKeyword = mutableStateOf<String>("")
    val searchKeyword : State<String> = _searchKeyword

    private var _crewSearchState = mutableStateOf(CrewSearchState())
    val crewSearchState : State<CrewSearchState> = _crewSearchState

    private var _impromptuSearchState = mutableStateOf(ImpromptuSearchState())
    val impromptuSearchState : State<ImpromptuSearchState> = _impromptuSearchState




    fun performSearch(selectedTab : Int, keyword : String) {
        //todo search from server.
        _searchKeyword.value = keyword
        Log.d("{SearchViewModel.performSearch}", "category : $selectedTab keyword : $keyword")

        when(selectedTab){
            0 -> searchCrew(keyword)
            else -> searchImpromptu(keyword)
        }


    }

    private fun searchCrew(keyword : String) = viewModelScope.launch {
        _crewSearchState.value = CrewSearchState(isLoading = true)
        delay(1200L)
        _crewSearchState.value = CrewSearchState()

    }

    private fun searchImpromptu(keyword : String) = viewModelScope.launch {
        _impromptuSearchState.value = ImpromptuSearchState(isLoading = true)
        delay(1200L)
        _impromptuSearchState.value = ImpromptuSearchState()

    }
}