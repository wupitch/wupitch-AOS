package wupitch.android.presentation.ui.main.impromptu.impromptu_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.domain.repository.ImprtRepository
import javax.inject.Inject

@HiltViewModel
class ImprtDetailViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository
) : ViewModel() {

    private var _imprtDetailState = mutableStateOf(ImprtDetailState())
    val imprtDetailState : State<ImprtDetailState> = _imprtDetailState

    fun getImprtDetail(id : Int) = viewModelScope.launch {
        _imprtDetailState.value = ImprtDetailState(isLoading = true)

        delay(1500L)
        val response = imprtRepository.getImprtDetail(id)
        if(response.isSuccessful){
            response.body()?.let { crewDetailRes ->
                if(crewDetailRes.isSuccess) _imprtDetailState.value = ImprtDetailState(data =
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
                else _imprtDetailState.value = ImprtDetailState(error = crewDetailRes.message)
            }
        }else _imprtDetailState.value = ImprtDetailState(error = "번개 조회에 실패했습니다.")
    }

    private var _joinImpromptuState = mutableStateOf(JoinImpromptuState())
    val joinImpromptuState : State<JoinImpromptuState> = _joinImpromptuState

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