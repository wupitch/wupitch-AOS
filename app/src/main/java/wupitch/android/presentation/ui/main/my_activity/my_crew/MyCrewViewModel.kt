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
import retrofit2.http.POST
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.ReportPostReq
import wupitch.android.data.remote.dto.toCrewDetailResult
import wupitch.android.data.remote.dto.toCrewMember
import wupitch.android.data.remote.dto.toResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.presentation.ui.main.home.crew_detail.CrewDetailState
import wupitch.android.util.GetImageFile
import wupitch.android.util.ReportType
import javax.inject.Inject

@HiltViewModel
class MyCrewViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    private val getRealPath: GetImageFile
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

//    /*
//    * pin
//    * */
//
//    private var _pinState = mutableStateOf(BaseState())
//    val pinState : State<BaseState> = _pinState
//
//    fun changePinStatus() = viewModelScope.launch {
//        _crewDetailState.value.data?.clubId?.let {
//            val response = crewRepository.changePinStatus(it)
//            if(response.isSuccessful){
//                response.body()?.let { res ->
//                    if(res.isSuccess) _pinState.value = BaseState(isSuccess = true)
//                    else  _pinState.value = BaseState(error = res.message)
//                }
//            }else _pinState.value = BaseState(error = "핀업에 실패했습니다.")
//        }
//    }


    /*
    * report
    * */

    private var _showReportDialog = MutableLiveData<Boolean>()
    val showReportDialog : LiveData<Boolean> = _showReportDialog

    private var reportType = ReportType.POST
    private var reportId = -1

    fun setShowReportDialog(reportType: ReportType, reportId : Int) {
        _showReportDialog.value = true
        this.reportType = reportType
        this.reportId = reportId
    }

    fun postReport(content : String) {
        _showReportDialog.value = false
        when(reportType){
            ReportType.POST -> reportPost(content, reportId)
            ReportType.CREW -> reportCrew(content)
        }
    }

    private fun reportCrew(content: String) = viewModelScope.launch {
        //todo
    }

    private var _postReportState = mutableStateOf(BaseState())
    val postReportState : State<BaseState> = _postReportState

    private fun reportPost(content: String, postId: Int) = viewModelScope.launch {
        _postReportState.value = BaseState(isLoading = true)

        val response = crewRepository.patchPostReport(
            postId = postId,
            reportPostReq = ReportPostReq(reportContents = content)
        )
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) _postReportState.value = BaseState(isSuccess = true)
                else _postReportState.value = BaseState(error = res.message)
            }
        }else _postReportState.value = BaseState(error = "신고하기에 실패했습니다.")

    }

    /*
    * crew board
    * */

    private var _crewPostState = mutableStateOf(CrewPostState())
    val crewPostState : State<CrewPostState> = _crewPostState

    fun getCrewPosts() = viewModelScope.launch {
        _crewPostState.value = CrewPostState(isLoading = true)

        val response = crewRepository.getCrewPosts(crewId)
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess){
                    _crewPostState.value = CrewPostState(
                        data = res.result.map { it.toResult() }
                    )
                }else _crewPostState.value = CrewPostState(error = res.message)
            }
        }else _crewPostState.value = CrewPostState(error ="게시글 조회에 실패했습니다.")
    }

    private var _postLikeState = mutableStateOf(BaseState())
    val postLikeState : State<BaseState> = _postLikeState

    fun patchPostLike(postId : Int) = viewModelScope.launch {

        _postLikeState.value = BaseState(isLoading = true)

        val response = crewRepository.patchPostLike(postId)
        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) _postLikeState.value = BaseState(isSuccess = true)
                else _postLikeState.value = BaseState(error = res.message)
            }
        } else _postLikeState.value = BaseState(error = "좋아요 등록/취소에 실패했습니다.")
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

    private var _memberState = mutableStateOf(CrewMemberState())
    val memberState : State<CrewMemberState> = _memberState

    fun getMembers() = viewModelScope.launch {
        _memberState.value = CrewMemberState(isLoading = true)
        val response = crewRepository.getCrewMembers(crewId)
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _memberState.value = CrewMemberState(data = res.result.map { it.toCrewMember() })
                }else  _memberState.value = CrewMemberState(error = res.message)
            }
        }else  _memberState.value = CrewMemberState(error = "멤버 조회에 실패했습니다.")
    }
}
