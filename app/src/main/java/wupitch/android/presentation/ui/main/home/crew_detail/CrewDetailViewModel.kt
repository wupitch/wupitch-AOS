package wupitch.android.presentation.ui.main.home.crew_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.data.remote.dto.CrewVisitorReq
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.util.*
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val userInfoDataStore : DataStore<Preferences>
) : ViewModel() {

    private var crewId: Int = -1
    private var creatorId : Int = -1

    private var _crewDetailState = mutableStateOf(CrewDetailState())
    val crewDetailState: State<CrewDetailState> = _crewDetailState

    fun getCrewDetail(id: Int) = viewModelScope.launch {
        _crewDetailState.value = CrewDetailState(isLoading = true)
        crewId = id

        val response = crewRepository.getCrewDetail(id)
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    _crewDetailState.value = CrewDetailState(
                        data = CrewDetailResult(
                            ageTable = convertedAge(res.result.ageTable),
                            areaName = res.result.areaName ?: "장소 미정",
                            clubId = res.result.clubId,
                            clubTitle = res.result.clubTitle,
                            crewImage = res.result.crewImage,
                            crewName = res.result.crewName ?: "",
                            dues = convertedCrewFee(res.result.dues, res.result.guestDues),
                            extraList = res.result.extraList,
                            introduction = res.result.introduction,
                            memberCount = "${res.result.memberCount}명",
                            schedules = convertedSchedule(res.result.schedules),
                            sportsId = res.result.sportsId - 1,
                            materials = res.result.materials,
                            inquiries = res.result.inquiries,
                            isPinUp = res.result.isPinUp,
                            isSelect = res.result.isSelect
                        )
                    )
                    creatorId = res.result.creatorAccountId
                }
                else _crewDetailState.value = CrewDetailState(error = res.message)
            }
        } else _crewDetailState.value = CrewDetailState(error = "크루 조회에 실패했습니다.")
    }
    

    private fun convertedSchedule(schedules: List<Schedule>): List<String> {
        val schedule = arrayListOf<String>()
        schedules.forEach {
            schedule.add("${it.day} ${doubleToTime(it.startTime)} - ${doubleToTime(it.endTime)}")
        }
        return schedule.toList()
    }

    private fun convertedCrewFee(dues: Int?, guestDues: Int?): List<String> {
        val list = arrayListOf<String>()
        val formatter: DecimalFormat =
            DecimalFormat("#,###")

        if (dues != null) {
            val formattedMoney = formatter.format(dues)
            list.add("정회원비 $formattedMoney 원")
        }
        if (guestDues != null) {
            val formattedMoney = formatter.format(guestDues)
            list.add("손님비 $formattedMoney 원")
        }

        return list.toList()
    }

    private fun convertedAge(ageTable: List<String>): String {

        val stringBuilder = StringBuilder()
        ageTable.forEachIndexed { index, s ->
            if (index != ageTable.size - 1) {
                stringBuilder.append("$s, ")
            } else {
                stringBuilder.append(s)
            }
        }
        return stringBuilder.toString()
    }

    private var _pinChangeState = mutableStateOf(BaseState())
    val pinChangeState: State<BaseState> = _pinChangeState

    fun changePinStatus() = viewModelScope.launch {
        _crewDetailState.value.data?.clubId?.let {
            val response = crewRepository.changePinStatus(it)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _pinChangeState.value = BaseState(isSuccess = true)
                    else _pinChangeState.value = BaseState(error = res.message)
                }
            } else _pinChangeState.value = BaseState(error = "핀업에 실패했습니다.")
        }
    }


    /*
    * participate
    * */

    private var _joinState = mutableStateOf(JoinState())
    val joinState: State<JoinState> = _joinState

    fun initJoinState() {
        _joinState.value = JoinState()
    }

    fun participateCrew() = viewModelScope.launch {
        _joinState.value = JoinState(isLoading = true)
        if(checkIsCreator()) {
            _joinState.value = JoinState(code = -100, error = "본인이 생성한 크루는 신청이 불가능해요")
            return@launch
        }
        _crewDetailState.value.data?.clubId?.let {
            val response = crewRepository.joinCrew(it)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) {
                        if (res.result.result) _joinState.value =
                            JoinState(isSuccess = true, result = true)
                        else _joinState.value = JoinState(isSuccess = true, result = false)
                    } else {
                        _joinState.value = JoinState(error = res.message, code = res.code)
                    }
                }
            } else _joinState.value = JoinState(error = "크루 참여에 실패했습니다.")
        }
    }

    private suspend fun checkIsCreator() : Boolean {
        val flow = userInfoDataStore.data.first()
        return flow[Constants.USER_ID] == creatorId
    }

    /*
    * visitor
    * */

    private var _visitDatesState = mutableStateOf(VisitDatesState())
    val visitDatesState: State<VisitDatesState> = _visitDatesState

    fun getVisitorDates() = viewModelScope.launch {
        _visitDatesState.value = VisitDatesState(isLoading = true)

        val response = crewRepository.getCrewVisitorDates(crewId)
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) {
                    _visitDatesState.value = VisitDatesState(
                        data = VisitorInfo(
                            dates = renderVisitDates(res.result.localDates, res.result.days),
                            dues = convertedGuestFee(res.result.guestDue)
                        )
                    )
                } else _visitDatesState.value = VisitDatesState(error = res.message)

            }
        } else _visitDatesState.value = VisitDatesState(error = "손님 참여 가능 날짜 조회를 실패했습니다.")

    }

    private fun renderVisitDates(dates: List<String>, days: List<String>): List<String> {
        val outputList = arrayListOf<String>()

        dates.forEachIndexed { index, s ->
            outputList.add("${dateDashToCol(s)} ${days[index]}")
        }

        return outputList.toList()
    }

    private fun convertedGuestFee(guestDues: Int): String {
        val formatter: DecimalFormat =
            DecimalFormat("#,###")
        return "${formatter.format(guestDues)}원"
    }

    //todo : response to be fixed in server
    private var _postVisitState = mutableStateOf(BaseState())
    val postVisitState : State<BaseState> = _postVisitState

    fun postVisit(date : String) = viewModelScope.launch {
        _postVisitState.value = BaseState(isLoading = true)
        if(checkIsCreator()) {
            _postVisitState.value = BaseState(error = "본인이 생성한 크루는 신청이 불가능해요")
            return@launch
        }
        val req = CrewVisitorReq(
            crewId = crewId,
            date = convertedVisitDate(date)
        )
        val response = crewRepository.postVisit(req)

        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) _postVisitState.value = BaseState(isSuccess = true)
                else _postVisitState.value = BaseState(error = res.message)
            }
        }else _postVisitState.value = BaseState(error = "손님신청에 실패했습니다.")

    }

    fun initPostVisitState() {
        _postVisitState.value = BaseState()
    }

    private fun convertedVisitDate(date : String) : String{
        val datePart = date.split(" ")[0]
        return "20${datePart.replace(".", "-")}"
    }
}