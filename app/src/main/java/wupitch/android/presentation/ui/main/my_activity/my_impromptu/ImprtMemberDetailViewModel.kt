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
import wupitch.android.domain.model.MemberDetail
import wupitch.android.domain.model.SportResult
import wupitch.android.domain.repository.ImprtRepository
import javax.inject.Inject

@HiltViewModel
class ImprtMemberDetailViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository
) : ViewModel() {

    var memberId = -1
    var imprtId = -1

    /*
    * get member info
    * */

    private var _memberInfoState = mutableStateOf(ImprtMemberDetailState())
    val memberInfoState: State<ImprtMemberDetailState> = _memberInfoState

    fun getMemberInfo() = viewModelScope.launch {
        _memberInfoState.value = ImprtMemberDetailState(isLoading = true)
        delay(500L)
        _memberInfoState.value = ImprtMemberDetailState(
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
                visitorDate = null
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