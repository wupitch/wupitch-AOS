package wupitch.android.presentation.ui.main.impromptu.impromptu_detail

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Constants.userInfoStore
import wupitch.android.domain.model.ImprtDetailResult
import wupitch.android.domain.repository.ImprtRepository
import wupitch.android.presentation.ui.main.home.crew_detail.JoinState
import wupitch.android.util.dateDashToCol
import wupitch.android.util.doubleToTime
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ImprtDetailViewModel @Inject constructor(
    private val imprtRepository: ImprtRepository,
    private val userInfoDataStore : DataStore<Preferences>
) : ViewModel() {

    private var creatorId : Int = -1

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
                    creatorId = res.result.creatorAccountId
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
    * pin up
    * */

    private var _pinState = mutableStateOf(BaseState())
    val pinState : State<BaseState> = _pinState

    fun changePinStatus() = viewModelScope.launch {
        _imprtDetailState.value.data?.impromptuId?.let {
            val response = imprtRepository.changePinStatus(it)
            if(response.isSuccessful){
                response.body()?.let { res ->
                    if(res.isSuccess) _pinState.value = BaseState(isSuccess = true)
                    else  _pinState.value = BaseState(error = res.message)
                }
            }else _pinState.value = BaseState(error = "핀업에 실패했습니다.")
        }

    }

    /*
    * participate
    * */

    private var _joinState = mutableStateOf(JoinState())
    val joinState : State<JoinState> = _joinState


    fun joinImprt() = viewModelScope.launch {
        _joinState.value = JoinState(isLoading = true)
        if(checkIsCreator()) {
            _joinState.value = JoinState(code = -100, error = "본인이 생성한 번개는 신청이 불가능해요")
            return@launch
        }

        _imprtDetailState.value.data?.impromptuId?.let {
            val response = imprtRepository.joinImprt(it)
            if(response.isSuccessful){
                response.body()?.let { res ->
                    if(res.isSuccess){
                        if(res.result.result)_joinState.value = JoinState(isSuccess = true, result = true)
                        else _joinState.value = JoinState(isSuccess = true, result = false)
                    }
                    else {
                        _joinState.value = JoinState(error = res.message, code = res.code)
                    }
                }
            }else _joinState.value = JoinState(error = "번개 참여에 실패했습니다.")
        }
    }

    fun initJoinState() {
        _joinState.value = JoinState()
    }

    private suspend fun checkIsCreator() : Boolean {
        val flow = userInfoDataStore.data.first()
        return flow[Constants.USER_ID] == creatorId
    }

}