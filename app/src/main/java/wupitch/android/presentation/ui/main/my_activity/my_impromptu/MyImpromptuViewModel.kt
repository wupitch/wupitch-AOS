package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.toCrewMember
import wupitch.android.data.remote.dto.toImprtDetailResult
import wupitch.android.data.remote.dto.toImprtMember
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.impromptu.impromptu_detail.ImprtDetailState
import wupitch.android.presentation.ui.main.my_activity.my_crew.CrewMemberState
import javax.inject.Inject

@HiltViewModel
class MyImpromptuViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository
) : ViewModel() {


    private var imprtId = -1

    private var _imprtDetailState = mutableStateOf(ImprtDetailState())
    val imprtDetailState : State<ImprtDetailState> = _imprtDetailState

    fun getImprtDetail(id : Int) = viewModelScope.launch {
        _imprtDetailState.value = ImprtDetailState(isLoading = true)
        imprtId = id

        val response = imprtRepository.getImprtDetail(id)
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _imprtDetailState.value = ImprtDetailState(
                        data = res.result.toImprtDetailResult()
                    )
                }
                else _imprtDetailState.value = ImprtDetailState(error = res.message)
            }
        }else _imprtDetailState.value = ImprtDetailState(error = "번개 조회에 실패했습니다.")
    }


    /*
   * report
   * */

    private var _showReportDialog = MutableLiveData<Boolean>()
    val showReportDialog : LiveData<Boolean> = _showReportDialog

    fun setShowReportDialog() {
        _showReportDialog.value = true
    }

    fun postImprtReport(content : String) {
        //todo
        _showReportDialog.value = false
        Log.d("{MyImpromptuViewModel.postImprtReport}", content.toString())
    }

    /*
   * members
   * */

    private var _memberState = mutableStateOf(ImprtMemberState())
    val memberState : State<ImprtMemberState> = _memberState

    fun getMembers() = viewModelScope.launch {
        _memberState.value = ImprtMemberState(isLoading = true)

        val response = imprtRepository.getImprtMembers(imprtId)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _memberState.value = ImprtMemberState(data = res.result.map { it.toImprtMember() })
                }else  _memberState.value = ImprtMemberState(error = res.message)
            }
        }else  _memberState.value = ImprtMemberState(error = "멤버 조회에 실패했습니다.")
    }
}