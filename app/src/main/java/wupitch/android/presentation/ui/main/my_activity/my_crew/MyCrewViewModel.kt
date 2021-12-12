package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.net.Uri
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
import wupitch.android.data.remote.dto.toCrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.presentation.ui.main.home.crew_detail.CrewDetailState
import wupitch.android.util.GetRealPath
import javax.inject.Inject

@HiltViewModel
class MyCrewViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val getRealPath: GetRealPath
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
                    data = res.result.toCrewDetailResult()
                )
                else _crewDetailState.value = CrewDetailState(error = res.message)
            }
        } else _crewDetailState.value = CrewDetailState(error = "크루 조회에 실패했습니다.")
    }

    /*
    * pin
    * */

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

    private var _showReportDialog = MutableLiveData<Boolean>()
    val showReportDialog : LiveData<Boolean> = _showReportDialog

    fun setShowReportDialog() {
        _showReportDialog.value = true
    }

    fun postCrewReport(content : String) {
        //todo
        _showReportDialog.value = false
        Log.d("{MyCrewViewModel.postCrewReport}", content.toString())
    }

    /*
    * crew board
    * */

    private var _crewPostState = mutableStateOf(CrewPostState())
    val crewPostState : State<CrewPostState> = _crewPostState

    fun getCrewPosts() = viewModelScope.launch {
        _crewPostState.value = CrewPostState(isLoading = true)

        delay(500L)
        _crewPostState.value = CrewPostState(
            data = listOf(
                CrewPost(
                    id = 1,
                    isAnnounce = true,
                    announceTitle = "회비 납부일은 매일 6월입니다.회비 납부일은 매일 6월",
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

    /*
    * post gallery image
    * */
    var shareOnlyToCrew = true

    fun setUserImage(uri: Uri) = viewModelScope.launch {
//        val path = getRealPath.getRealPathFromURIForGallery(uri)
//
//        if (path != null) {
//            resizeImage(file = File(path))
//
//            val file = getImageBody(File(path))

//            val response = profileRepository.postProfileImage(file.body, file)
//            if (response.isSuccessful) {
//                response.body()?.let { res ->
//                    if (res.isSuccess) _userImageState.value = BaseState(isSuccess = true)
//                    else _userImageState.value = BaseState(error = res.message)
//                }
//            } else _userImageState.value = BaseState(error = "프로필 이미지 업로드를 실패했습니다.")
//        }

    }

//    private fun resizeImage(file: File, scaleTo: Int = 1024) {
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(file.absolutePath, bmOptions)
//        val photoW = bmOptions.outWidth
//        val photoH = bmOptions.outHeight
//
//        val scaleFactor = Math.min(photoW / scaleTo, photoH / scaleTo)
//
//        bmOptions.inJustDecodeBounds = false
//        bmOptions.inSampleSize = scaleFactor
//
//        val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return
//        file.outputStream().use {
//            resized.compress(Bitmap.CompressFormat.JPEG, 75, it)
//            resized.recycle()
//        }
//    }

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

data class Member(
    val id : Int,
    val userImage : String?,
    val userName : String,
    val isLeader : Boolean
)

data class MemberState(
    val isLoading : Boolean = false,
    val data : List<Member> = emptyList(),
    val error : String = ""
)
