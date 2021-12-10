package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.Schedule
import wupitch.android.domain.model.CrewDetailResult
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.presentation.ui.main.home.crew_detail.CrewDetailState
import wupitch.android.util.doubleToTime
import java.io.File
import java.lang.StringBuilder
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MyCrewViewModel @Inject constructor(
    private val crewRepository: CrewRepository,
    @ApplicationContext val context : Context
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
        val path = getRealPathFromURIForGallery(uri)

        if (path != null) {
            resizeImage(file = File(path))

            val file = getImageBody(File(path))

//            val response = profileRepository.postProfileImage(file.body, file)
//            if (response.isSuccessful) {
//                response.body()?.let { res ->
//                    if (res.isSuccess) _userImageState.value = BaseState(isSuccess = true)
//                    else _userImageState.value = BaseState(error = res.message)
//                }
//            } else _userImageState.value = BaseState(error = "프로필 이미지 업로드를 실패했습니다.")
        }

    }

    private fun resizeImage(file: File, scaleTo: Int = 1024) {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        val scaleFactor = Math.min(photoW / scaleTo, photoH / scaleTo)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return
        file.outputStream().use {
            resized.compress(Bitmap.CompressFormat.JPEG, 75, it)
            resized.recycle()
        }
    }

    private fun getImageBody(file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "images",
            filename = file.name,
            body = file.asRequestBody("image/*".toMediaType())
        )
    }

    private fun getRealPathFromURIForGallery(uri: Uri): String? {

        var fullPath: String? = null
        val column = "_data"
        var cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            var documentId = cursor.getString(0)
            if (documentId == null) {
                for (i in 0 until cursor.columnCount) {
                    if (column.equals(cursor.getColumnName(i), ignoreCase = true)) {
                        fullPath = cursor.getString(i)
                        break
                    }
                }
            } else {
                documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
                cursor.close()
                val projection = arrayOf(column)
                try {
                    cursor = context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        MediaStore.Images.Media._ID + " = ? ",
                        arrayOf(documentId),
                        null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()
                        fullPath = cursor.getString(cursor.getColumnIndexOrThrow(column))
                    }
                } finally {
                    if (cursor != null) cursor.close()
                }
            }
        }
        return fullPath
    }
}
