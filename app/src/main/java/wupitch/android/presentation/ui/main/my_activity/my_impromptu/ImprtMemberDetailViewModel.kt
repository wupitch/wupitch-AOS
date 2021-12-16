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
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.toMemberDetail
import wupitch.android.domain.model.MemberDetail
import wupitch.android.domain.model.SportResult
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.my_activity.my_crew.CrewMemberDetailState
import javax.inject.Inject

@HiltViewModel
class ImprtMemberDetailViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository
) : ViewModel() {

    var memberId = -1
    var imprtId = -1
    var isCurrentUserLeader = false

    /*
    * get member info
    * */

    private var _memberInfoState = mutableStateOf(ImprtMemberDetailState())
    val memberInfoState: State<ImprtMemberDetailState> = _memberInfoState

    fun getMemberInfo() = viewModelScope.launch {
        _memberInfoState.value = ImprtMemberDetailState(isLoading = true)
        val response = imprtRepository.getImprtMemberDetail(imprtId, memberId)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _memberInfoState.value = ImprtMemberDetailState(
                        data = res.result.toMemberDetail()
                    )
                }else  _memberInfoState.value = ImprtMemberDetailState(error = res.message)
            }
        }else  _memberInfoState.value = ImprtMemberDetailState(error = "멤버 조회에 실패했습니다.")
    }

    /*
    * report
    * */

    private var _showReportDialog = MutableLiveData<Boolean>()
    val showReportDialog: LiveData<Boolean> = _showReportDialog

    fun setShowReportDialog() {
        _showReportDialog.value = true
    }

    fun postImprtReport(content: String) {
        //todo
        _showReportDialog.value = false
    }

    /**
     * dismiss member
     */

    private var _dismissMemberState = mutableStateOf(BaseState())
    val dismissMember : State<BaseState> = _dismissMemberState

    fun dismissMember() = viewModelScope.launch {
        _dismissMemberState.value = BaseState(isLoading = true)

        val response = imprtRepository.dismissMember(imprtId, memberId)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _dismissMemberState.value = BaseState(isSuccess = true)
                else _dismissMemberState.value = BaseState(error = res.message)
            }
        }else _dismissMemberState.value = BaseState(error =  "멤버 삭제에 실패했습니다.")
    }


}