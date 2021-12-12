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
import wupitch.android.data.remote.dto.toImprtDetailResult
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.impromptu.impromptu_detail.ImprtDetailState
import wupitch.android.presentation.ui.main.my_activity.my_crew.Member
import wupitch.android.presentation.ui.main.my_activity.my_crew.MemberState
import javax.inject.Inject

@HiltViewModel
class MyImpromptuViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository
) : ViewModel() {


    private var _imprtDetailState = mutableStateOf(ImprtDetailState())
    val imprtDetailState : State<ImprtDetailState> = _imprtDetailState

    fun getImprtDetail(id : Int) = viewModelScope.launch {
        _imprtDetailState.value = ImprtDetailState(isLoading = true)

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

    private var _memberState = mutableStateOf(MemberState())
    val memberState : State<MemberState> = _memberState

    fun getMembers() = viewModelScope.launch {
        _memberState.value = MemberState(isLoading = true)
        delay(500L)
        _memberState.value = MemberState(data = listOf(
            Member(0,"https://blog.kakaocdn.net/dn/GUa7H/btqCpRytcqf/brPCKwItrfGNw1aWd8ZKb0/img.jpg", "베키", true),
            Member(1,null, "플로라", false),
            Member(2,null, "스완", false),
            Member(3,null, "우피치", false),
            Member(4,null, "우피치", false),
            Member(5,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),
            Member(6,null, "우피치", false),

            ))
    }
}