package wupitch.android.presentation.ui.main.my_activity.my_crew

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.presentation.ui.main.home.crew_detail.CrewDetailState
import wupitch.android.util.doubleToTime
import java.lang.StringBuilder
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MyCrewViewModel @Inject constructor(
    private val crewRepository: CrewRepository
) : ViewModel(){

    var crewId = -1
    var selectedTab = 0

    /*
    * crew intro (crew detail)
    * */

    private var _crewDetailState = mutableStateOf(CrewDetailState())
    val crewDetailState: State<CrewDetailState> = _crewDetailState

    fun getCrewDetail() = viewModelScope.launch {
        _crewDetailState.value = CrewDetailState(isLoading = true)

        val response = crewRepository.getCrewDetail(crewId)
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
                        extraList = res.result.extraList,
                        introduction = res.result.introduction,
                        memberCount = "${res.result.memberCount}명",
                        schedules = convertedSchedule(res.result.schedules),
                        sportsId = res.result.sportsId-1,
                        materials = res.result.materials,
                        inquiries = res.result.inquiries,
                        isPinUp = res.result.isPinUp,
                        isSelect = res.result.isSelect
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

    private var _pinState = mutableStateOf(BaseState())
    val pinState : State<BaseState> = _pinState

    fun changePinStatus() = viewModelScope.launch {
        _crewDetailState.value.data?.clubId?.let {
            val response = crewRepository.changePinStatus(it)
            if(response.isSuccessful){
                response.body()?.let { res ->
                    if(res.isSuccess) _pinState.value = BaseState(isSuccess = true)
                    else  _pinState.value = BaseState(error = res.message)
                }
            }else _pinState.value = BaseState(error = "핀업에 실패했습니다.")
        }
    }


    /*
    * report
    * */

    var crewReportState = mutableStateOf(false)

    private var _imprtReportState = mutableStateOf(false)
    val imprtReportState : State<Boolean> = _imprtReportState

    fun setCrewReportState() {
        crewReportState.value = true
    }

    fun postCrewReport(content : String) {

    }

    /*
    * crew board
    * */

    private var _crewPostState = mutableStateOf(CrewPostState())
    val crewPostState : State<CrewPostState> = _crewPostState

    fun getCrewPosts() = viewModelScope.launch {
        _crewPostState.value = CrewPostState(isLoading = true)

        delay(800L)
        _crewPostState.value = CrewPostState(
            data = listOf(
                CrewPost(
                    id = 1,
                    isAnnounce = true,
                    announceTitle = "회비 납부일은 매일 6월입니다.",
                    userImage = null,
                    userName = "베키짱",
                    isLeader = true,
                    content = "xx은행으로 입금해주시면 감사감사링하겠습니당~~!!! 여러분들 항상 즐거운 하루보내시구 담주에 봐용~",
                    isLiked = true,
                    likedNum = 30,
                    date = "21.12.03"
                ),
                CrewPost(
                    id = 2,
                    isAnnounce = false,
                    announceTitle = null,
                    userImage = null,
                    userName = "베키짱2",
                    isLeader = true,
                    content = "오늘 개꿀잼이었습니다 ㅋㅋㅋㅋ",
                    isLiked = false,
                    likedNum = 32,
                    date = "21.11.12"
                ),
                CrewPost(
                    id = 2,
                    isAnnounce = false,
                    announceTitle = null,
                    userImage = null,
                    userName = "베키짱2",
                    isLeader = true,
                    content = "오늘 개꿀잼이었습니다 ㅋㅋㅋㅋ",
                    isLiked = false,
                    likedNum = 32,
                    date = "21.11.12"
                ),
                CrewPost(
                    id = 2,
                    isAnnounce = false,
                    announceTitle = null,
                    userImage = null,
                    userName = "베키짱2",
                    isLeader = true,
                    content = "오늘 개꿀잼이었습니다 ㅋㅋㅋㅋ",
                    isLiked = false,
                    likedNum = 32,
                    date = "21.11.12"
                ),
                CrewPost(
                    id = 2,
                    isAnnounce = false,
                    announceTitle = null,
                    userImage = null,
                    userName = "베키짱2",
                    isLeader = true,
                    content = "오늘 개꿀잼이었습니다 ㅋㅋㅋㅋ",
                    isLiked = false,
                    likedNum = 32,
                    date = "21.11.12"
                )
            )
        )

    }
}
