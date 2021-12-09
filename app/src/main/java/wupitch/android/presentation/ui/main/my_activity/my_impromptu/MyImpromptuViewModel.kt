package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.domain.model.ImprtDetailResult
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.impromptu.impromptu_detail.ImprtDetailState
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import java.text.DecimalFormat
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
                        data = ImprtDetailResult(
                            date = "${dateDashToCol(res.result.date)} ${res.result.day}",
                            time = "${doubleToTime(res.result.startTime)} - ${doubleToTime(res.result.endTime)}",
                            dday = res.result.dday,
                            dues = if (res.result.dues == null) null else convertedFee(res.result.dues),
                            impromptuId = res.result.impromptuId,
                            impromptuImage = res.result.impromptuImage,
                            inquiries = res.result.inquiries,
                            introduction = res.result.introduction,
                            location = res.result.location ?: "장소 미정",
                            materials = res.result.materials,
                            recruitStatus = "${res.result.nowMemberCount}/${res.result.recruitmentCount}명 참여",
                            title = res.result.title,
                            isPinUp = res.result.isPinUp,
                            isSelect = res.result.isSelect
                        )
                    )
                }
                else _imprtDetailState.value = ImprtDetailState(error = res.message)
            }
        }else _imprtDetailState.value = ImprtDetailState(error = "번개 조회에 실패했습니다.")
    }

    private fun convertedFee(guestDues: Int?):String{
        val formatter: DecimalFormat =
            DecimalFormat("#,###")
        return "참여비 ${formatter.format(guestDues)}원"
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

}