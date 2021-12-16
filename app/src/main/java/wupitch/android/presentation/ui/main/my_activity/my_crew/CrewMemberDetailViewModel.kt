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
import wupitch.android.data.remote.dto.toMemberDetail
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
                }else  _memberInfoState.value = CrewMemberDetailState(error = res.message)
            }
        }else  _memberInfoState.value = CrewMemberDetailState(error = "멤버 조회에 실패했습니다.")
    }

    private fun getImprtMemberInfo() {

    }

    private fun getCrewMemberInfo() {

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
        Log.d("{MyImpromptuViewModel.postImprtReport}", content.toString())
    }

    /**
     * dismiss member
     */

    fun dismissMember() {

    }

    /*
    * visitor management
    * */

    fun acceptVisitor () {

    }

    fun declineVisitor() {

    }

    /*
    * member-to-be management
    * */

    fun acceptMemberToBe () {

    }

    fun declineMemberToBe() {

    }




}