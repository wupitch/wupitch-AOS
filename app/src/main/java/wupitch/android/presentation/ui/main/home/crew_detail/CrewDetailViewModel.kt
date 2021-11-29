package wupitch.android.presentation.ui.main.home.crew_detail

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.util.*
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _crewDetailState = mutableStateOf(CrewDetailState())
    val crewDetailState: State<CrewDetailState> = _crewDetailState

    fun getCrewDetail(id: Int) = viewModelScope.launch {
        _crewDetailState.value = CrewDetailState(isLoading = true)

        val response = crewRepository.getCrewDetail(id)
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) _crewDetailState.value = CrewDetailState(
                    data = CrewDetailResult(
                        ageTable = convertedAge(res.result.ageTable),
                        areaName = res.result.areaName ?: "장소 미정",
                        clubId = res.result.clubId,
                        clubTitle = res.result.clubTitle,
                        crewImage = res.result.crewImage,
                        crewName = res.result.crewName ?: "",
                        dues = convertedCrewFee(res.result.dues, res.result.guestDues),
                        guestDues = convertedGuestFee(res.result.guestDues),
                        extraList = res.result.extraList,
                        introduction = res.result.introduction,
                        memberCount = "${res.result.memberCount}명",
                        schedules = convertedSchedule(res.result.schedules),
                        sportsId = res.result.sportsId - 1,
                        materials = res.result.materials,
                        inquiries = res.result.inquiries,
                        visitDays = convertedVisitorDays(res.result.schedules)
                    )
                )
                else _crewDetailState.value = CrewDetailState(error = res.message)
            }
        } else _crewDetailState.value = CrewDetailState(error = "크루 조회에 실패했습니다.")
    }

    private fun convertedVisitorDays(schedules: List<Schedule>): List<String> {
        val calendar: Calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        val visitDays = arrayListOf<String>()
        schedules.forEachIndexed { index, item ->

            if (index < 3) {
                val diff = convertDay(item.dayIdx) - today
                val tempCal: Calendar = Calendar.getInstance()
                tempCal.add(Calendar.DAY_OF_YEAR, diff)

                if (diff <= 0) { //정기일정이 이미 지난 경우
                    tempCal.add(Calendar.WEEK_OF_YEAR, 1)
                }
                val year = tempCal.get(Calendar.YEAR) - 2000
                val month = tempCal.get(Calendar.MONTH) + 1
                val date = tempCal.get(Calendar.DATE)
                val day = koreanFullDay(tempCal.get(Calendar.DAY_OF_WEEK))

                val newMonth = if (month < 10) "0$month" else "$month"
                val newDate = if (date < 10) "0$date" else "$date"

                visitDays.add("$year.$newMonth.$newDate $day")
            }
        }
        return visitDays
    }

    private fun convertedSchedule(schedules: List<Schedule>): List<String> {
        val schedule = arrayListOf<String>()
        schedules.forEach {
            schedule.add("${it.day} ${doubleToTime(it.startTime)} - ${doubleToTime(it.endTime)}")
        }
        return schedule.toList()
    }

    private fun convertedGuestFee(guestDues: Int?): String {
        if (guestDues == null) return "0원"
        val formatter: DecimalFormat =
            DecimalFormat("#,###")
        return "${formatter.format(guestDues)}원"
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

    private var _pinState = mutableStateOf(BaseState())
    val pinState: State<BaseState> = _pinState

    fun changePinStatus() = viewModelScope.launch {
        _crewDetailState.value.data?.clubId?.let {
            val response = crewRepository.changePinStatus(it)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _pinState.value = BaseState(isSuccess = true)
                    else _pinState.value = BaseState(error = res.message)
                }
            } else _pinState.value = BaseState(error = "핀업에 실패했습니다.")
        }
    }

    fun setNotEnoughInfo() = viewModelScope.launch {

        context.dataStore.edit { settings ->
            settings[Constants.FIRST_COMER] = true
        }
    }

    /*
    * participate
    * */

    private var _joinState = mutableStateOf(JoinState())
    val joinState : State<JoinState> = _joinState

    fun participateCrew() = viewModelScope.launch {
        _joinState.value = JoinState(isLoading = true)
        _crewDetailState.value.data?.clubId?.let {
            val response = crewRepository.joinCrew(it)
            if(response.isSuccessful){
                response.body()?.let { res ->
                    if(res.isSuccess) _joinState.value = JoinState(isSuccess = true)
                    else {
                        _joinState.value = JoinState(error = res.message, code = res.code)
                    }
                }
            }else _joinState.value = JoinState(error = "크루 참여에 실패했습니다.")
        }
    }
}