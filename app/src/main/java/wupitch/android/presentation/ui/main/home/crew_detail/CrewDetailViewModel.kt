package wupitch.android.presentation.ui.main.home.crew_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.util.doubleToTime
import wupitch.android.util.formatToWon
import java.lang.StringBuilder
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val crewRepository: CrewRepository
) : ViewModel() {

    private var _crewDetailState = mutableStateOf(CrewDetailState())
    val crewDetailState: State<CrewDetailState> = _crewDetailState

    fun getCrewDetail(id: Int) = viewModelScope.launch {
        _crewDetailState.value = CrewDetailState(isLoading = true)

        //todo materials and inquiries to be added.
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
                        sportsId = res.result.sportsId-1,
                        materials = res.result.materials,
                        inquiries = res.result.inquiries
                    )
                )
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
    private fun convertedGuestFee(guestDues: Int?):String{
        if(guestDues == null) return "0원"
        val formatter: DecimalFormat =
            DecimalFormat("#,###")
        return "${formatter.format(guestDues)}원"
    }

    private fun convertedCrewFee(dues: Int?, guestDues: Int? ): List<String> {
        val list = arrayListOf<String>()
        val formatter: DecimalFormat =
            DecimalFormat("#,###")

        if (dues != null){
            val formattedMoney = formatter.format(dues)
            list.add("정회원비 $formattedMoney 원")
        }
        if(guestDues != null){
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
            }else {
                stringBuilder.append(s)
            }
        }
        return stringBuilder.toString()
    }


}