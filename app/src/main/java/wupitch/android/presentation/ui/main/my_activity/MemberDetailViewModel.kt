package wupitch.android.presentation.ui.main.my_activity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.domain.model.SportResult

data class MemberDetailState(
    val isLoading: Boolean = false,
    val data: MemberDetail? = null,
    val error: String = ""
)

data class MemberDetail(
    val userImage: String?,
    val userName: String,
    val userAgeGroup: String,
    val userArea: String,
    val userPhoneNum: String,
    val userSports: List<SportResult>,
    val intro: String
)

class MemberDetailViewModel : ViewModel() {

    /*
    * get member info
    * */

    private var _memberInfoState = mutableStateOf(MemberDetailState())
    val memberInfoState: State<MemberDetailState> = _memberInfoState

    fun getMemberInfo(id: Int) = viewModelScope.launch {
        _memberInfoState.value = MemberDetailState(isLoading = true)
        delay(500L)
        _memberInfoState.value = MemberDetailState(
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
                intro = "할룽할룽~! 사람들이랑 같이 운동하는거 넘 좋아한다능 ㅎㅎ"
            )
        )
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
}