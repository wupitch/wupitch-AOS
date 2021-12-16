package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.domain.model.MemberDetail
import wupitch.android.domain.model.SportResult

class CrewMemberDetailViewModel : ViewModel() {

    var memberId = -1
    var crewId = -1

    /*
    * get member info
    * */

    private var _memberInfoState = mutableStateOf(CrewMemberDetailState())
    val memberInfoState: State<CrewMemberDetailState> = _memberInfoState

    fun getMemberInfo() = viewModelScope.launch {
        _memberInfoState.value = CrewMemberDetailState(isLoading = true)
        delay(500L)
        _memberInfoState.value = CrewMemberDetailState(
            data = MemberDetail(
                userImage = null,
                userName = "베키",
                userAgeGroup = "20대",
                userArea = "성북구",
                userPhoneNum = "01099998888",
                userSports = listOf(
                    SportResult("축구", 0),
                    SportResult("농구", 3),
                    SportResult("배구", 2),
                    SportResult("배드민턴", 1),
                    SportResult("런닝", 5),
                    SportResult("등산", 4),
                ),
                intro = "할룽할룽~! 사람들이랑 같이 운동하는거 넘 좋아한다능 ㅎㅎ",
                visitorDate = "21.00.00"
            )
        )
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