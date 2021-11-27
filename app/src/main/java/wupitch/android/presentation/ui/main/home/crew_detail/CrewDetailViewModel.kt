package wupitch.android.presentation.ui.main.home.crew_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.CrewDetailResult
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewRepository: CrewRepository
) : ViewModel() {

    private var _crewDetailState = mutableStateOf(CrewDetailState())
    val crewDetailState : State<CrewDetailState> = _crewDetailState

    fun getCrewDetail(id : Int) = viewModelScope.launch {
        _crewDetailState.value = CrewDetailState(isLoading = true)

        delay(1500L)
        val response = crewRepository.getCrewDetail(id)
        if(response.isSuccessful){
            response.body()?.let { crewDetailRes ->
                if(crewDetailRes.isSuccess) _crewDetailState.value = CrewDetailState(data =
//                CrewDetailResult(
//                    ageTable= List<String>,
//                    areaName= String,
//                clubId= Int,
//                clubTitle= String,
//                crewImage= String,
//                crewName= String,
//                dues= Int,
//                extraList= List<String>,
//                guestDues= Int,
//                introduction= String,
//                memberCount= Int,
//                schedules= List<Schedule>,
//                sportsId= Int,
//                sportsName= String
//                )

                crewDetailRes.result)
                else _crewDetailState.value = CrewDetailState(error = crewDetailRes.message)
            }
        }else _crewDetailState.value = CrewDetailState(error = "크루 조회에 실패했습니다.")
    }

}