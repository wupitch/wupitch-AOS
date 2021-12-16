package wupitch.android.presentation.ui.main.my_activity.my_crew

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
import wupitch.android.data.remote.dto.CrewApplicantReq
import wupitch.android.data.remote.dto.toMemberDetail
import wupitch.android.domain.model.CrewMemberStatus
import wupitch.android.domain.model.MemberDetail
import wupitch.android.domain.model.SportResult
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

@HiltViewModel
class CrewMemberDetailViewModel @Inject constructor(
    private val crewRepository: CrewRepository
) : ViewModel() {

    var memberId = -1
    var crewId = -1
    var isCurrentUserLeader = false
    var memberStatus = CrewMemberStatus()



    /*
    * get member info
    * */

    private var _memberInfoState = mutableStateOf(CrewMemberDetailState())
    val memberInfoState: State<CrewMemberDetailState> = _memberInfoState

    fun getMemberInfo() = viewModelScope.launch {
        _memberInfoState.value = CrewMemberDetailState(isLoading = true)
        val response = crewRepository.getCrewMemberDetail(crewId, memberId)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _memberInfoState.value = CrewMemberDetailState(
                        data = res.result.toMemberDetail()
                    )
                    isCurrentUserLeader = res.result.isAuthAccountLeader
                    memberStatus = CrewMemberStatus(
                        isValid = res.result.isValid,
                        isGuest = res.result.isGuest
                    )
                }else  _memberInfoState.value = CrewMemberDetailState(error = res.message)
            }
        }else  _memberInfoState.value = CrewMemberDetailState(error = "멤버 조회에 실패했습니다.")
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

    fun dismissMember() {
        //todo 멤버 삭제
    }

    /*
    * accept & decline guest and members
    * */

    private var _acceptState = mutableStateOf(BaseState())
    val acceptState : State<BaseState> = _acceptState


    fun accept(isGuest : Boolean) = viewModelScope.launch{
        _acceptState. value = BaseState(isLoading = true)
        val response = crewRepository.acceptCrewApplicant(CrewApplicantReq(
            accountId = memberId,
            clubId = crewId,
            isGuest = isGuest
        ))
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _acceptState.value = BaseState(isSuccess = true)
                else  _acceptState.value = BaseState(error = res.message)
            }
        }else _acceptState.value = BaseState(error = "수락에 실패했습니다.")

    }


    private var _declineState = mutableStateOf(BaseState())
    val declineState : State<BaseState> = _declineState


    fun decline(isGuest : Boolean) = viewModelScope.launch {
        val response = crewRepository.dismissCrewApplicant(
            CrewApplicantReq(
                accountId = memberId,
                clubId = crewId,
                isGuest = isGuest
            )
        )
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _declineState.value = BaseState(isSuccess = true)
                else  _declineState.value = BaseState(error = res.message)
            }
        }else _declineState.value = BaseState(error = "거절에 실패했습니다.")

    }




}