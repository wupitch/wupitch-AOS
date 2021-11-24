package wupitch.android.presentation.ui.main.home.create_crew

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Constants.EMPTY_IMAGE_URI
import wupitch.android.data.remote.dto.toFilterItem
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.Schedule
import wupitch.android.domain.repository.CrewRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.util.TimeType
import wupitch.android.util.isEndTimeFasterThanStart
import wupitch.android.util.stringToDouble
import wupitch.android.util.wonToNum
import java.io.File
import javax.inject.Inject
import android.provider.MediaStore.MediaColumns




@HiltViewModel
class CreateCrewViewModel @Inject constructor(
    private val getSportRepository: GetSportRepository,
    private val getDistrictRepository: GetDistrictRepository,
    private val crewRepository: CrewRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _crewSportId = mutableStateOf(-1)
    val crewSportId: State<Int> = _crewSportId

    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _crewDistrictId = MutableLiveData<Int>()
    val crewDistrictId: LiveData<Int> = _crewDistrictId

    private var _crewDistrictName = MutableLiveData<String>()
    val crewDistrictName: LiveData<String> = _crewDistrictName

    private var _crewLocation = mutableStateOf("")
    val crewLocation: State<String> = _crewLocation

    private var _crewName = mutableStateOf("")
    val crewName: State<String> = _crewName

    private var _crewSize = mutableStateOf("")
    val crewSize: State<String> = _crewSize

    private var _crewAgeGroupList = mutableStateListOf<Int>()
    val crewAgeGroupList: SnapshotStateList<Int> = _crewAgeGroupList

    private var _crewExtraInfoList = mutableStateListOf<Int>()
    val crewExtraInfoList: SnapshotStateList<Int> = _crewExtraInfoList

    private var _crewTitle = mutableStateOf("")
    val crewTitle: State<String> = _crewTitle

    private var _crewIntro = mutableStateOf("")
    val crewIntro: State<String> = _crewIntro

    private var _crewSupplies = mutableStateOf("")
    val crewSupplies: State<String> = _crewSupplies

    private var _crewInquiry = mutableStateOf("")
    val crewInquiry: State<String> = _crewInquiry


    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage: State<Boolean?> = _isUsingDefaultImage

    private var _imageChosenState = mutableStateOf(false)
    val imageChosenState: State<Boolean> = _imageChosenState

    private var _crewImage = mutableStateOf<Uri>(Constants.EMPTY_IMAGE_URI)
    val crewImage: State<Uri> = _crewImage

    private var _scheduleList = mutableStateListOf<ScheduleState>(
        ScheduleState(
            day = mutableStateOf(-1),
            startTime = mutableStateOf("00:00"),
            endTime = mutableStateOf("00:00"),
            isStartTimeSet = mutableStateOf<Boolean?>(false),
            isEndTimeSet = mutableStateOf<Boolean?>(false)
        )
    )
    val scheduleList: SnapshotStateList<ScheduleState> = _scheduleList

    private var _createCrewState = mutableStateOf(CreateCrewState())
    val createCrewState: State<CreateCrewState> = _createCrewState


    fun getSports() = viewModelScope.launch {
        _sportsList.value = SportState(isLoading = true)

        val response = getSportRepository.getSport()
        if (response.isSuccessful) {
            response.body()?.let { sportRes ->
                if (sportRes.isSuccess) {
                    _sportsList.value =
                        SportState(data = sportRes.result.filter { it.sportsId < sportRes.result.size }
                            .map { it.toFilterItem() })
                    _sportsList.value.data.forEachIndexed { index, filterItem ->
                        if (_crewSportId.value == index) {
                            filterItem.state.value = true
                        }
                    }
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }


    fun getDistricts() = viewModelScope.launch {
        _districtList.value = DistrictState(isLoading = true)

        val response = getDistrictRepository.getDistricts()
        if (response.isSuccessful) {
            response.body()?.let { districtRes ->
                if (districtRes.isSuccess) _districtList.value =
                    DistrictState(data = districtRes.result.map { it.name }.toTypedArray())
                else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")
            }
        } else _districtList.value = DistrictState(error = "지역 가져오기를 실패했습니다.")

    }

    fun setCrewSport(sportId: Int) {
        _crewSportId.value = sportId
        Log.d("{CreateCrewViewModel.setCrewSport}", _crewSportId.value.toString())
    }

    fun setCrewDistrict(districtId: Int, districtName: String) {
        //todo 서버에 보내기
        _crewDistrictId.value = districtId
        _crewDistrictName.value = districtName
        Log.d("{CreateCrewViewModel.setUserDistrict}", "id : $districtId name : $districtName")
    }

    fun setCrewLocation(location: String) {
        //todo 장소 미정이면 서버한테 null or 장소미정  text 보내야 되나?
        _crewLocation.value = location
        Log.d("{CreateCrewViewModel.setCrewLocation}", _crewLocation.value)
    }

    fun setCrewName(name: String) {
        _crewName.value = name
    }

    fun setCrewSize(size: String) {
        _crewSize.value = size
    }

    fun setCrewAgeGroupList(list: SnapshotStateList<Int>) {
        _crewAgeGroupList = list
        Log.d("{CreateCrewViewModel.setCrewAgeGroupList}", _crewAgeGroupList.toList().toString())
    }

    fun setCrewExtraInfoList(list: SnapshotStateList<Int>) {
        _crewExtraInfoList = list
        Log.d("{CreateCrewViewModel.setCrewExtraInfoList}", _crewExtraInfoList.toList().toString())
    }

    fun addCrewSchedule() {
        _scheduleList.add(
            ScheduleState(
                day = mutableStateOf(-1),
                startTime = mutableStateOf("00:00"),
                endTime = mutableStateOf("00:00"),
                isStartTimeSet = mutableStateOf<Boolean?>(false),
                isEndTimeSet = mutableStateOf<Boolean?>(false)
            )
        )
    }

    fun setCrewImage(image: Uri) {
        _crewImage.value = image
    }

    fun setIsUsingDefaultImage(isUsingDefaultImage: Boolean?) {
        _isUsingDefaultImage.value = isUsingDefaultImage
    }

    fun setImageChosenState(isImageChosen: Boolean) {
        _imageChosenState.value = isImageChosen
    }

    fun setTimeFilter(index: Int, type: TimeType, hour: Int, min: Int) {
        Log.d("{CreateCrewViewModel.setTimeFilter}", "hour : $hour min : $min")
        var hourString = hour.toString()
        var minString = min.toString()
        if (hour < 10) hourString = "0$hour"
        if (min < 10) minString = "0$min"
        val timeString = "$hourString:$minString"

        val schedule = scheduleList[index]

        when (type) {
            TimeType.START -> {
                if (schedule.isEndTimeSet.value == true) {
                    if (isEndTimeFasterThanStart(timeString, schedule.endTime.value)) {
                        schedule.startTime.value = "00:00"
                        schedule.isStartTimeSet.value = null
                    } else {
                        schedule.startTime.value = timeString
                        schedule.isStartTimeSet.value = true
                    }
                } else {
                    schedule.startTime.value = timeString
                    schedule.isStartTimeSet.value = true
                }
            }
            TimeType.END -> {

                if (isEndTimeFasterThanStart(schedule.startTime.value, timeString)) {
                    schedule.endTime.value = "00:00"
                    schedule.isEndTimeSet.value = null
                } else {
                    schedule.endTime.value = timeString
                    schedule.isEndTimeSet.value = true
                }
            }
        }
    }

    fun setCrewTitle(title: String) {
        _crewTitle.value = title
    }

    fun setCrewIntro(intro: String) {
        _crewIntro.value = intro
    }

    fun setCrewSupplies(supplies: String) {
        _crewSupplies.value = supplies
    }

    fun setCrewInquiry(inquiry: String) {
        _crewInquiry.value = inquiry
    }

    private var _crewFee = mutableStateOf<String>("")
    val crewFee: State<String> = _crewFee

    private var _noCrewFee = mutableStateOf(false)
    val noCrewFee: State<Boolean> = _noCrewFee

    private var _crewVisitorFee = mutableStateOf<String>("")
    val crewVisitorFee: State<String> = _crewVisitorFee

    private var _noCrewVisitorFee = mutableStateOf(false)
    val noCrewVisitorFee: State<Boolean> = _noCrewVisitorFee

    fun setCrewFee(money: String, toggleState: Boolean) {
        _crewFee.value = money
        _noCrewFee.value = toggleState
    }

    fun setCrewVisitorFee(money: String, toggleState: Boolean) {
        _crewVisitorFee.value = money
        _noCrewVisitorFee.value = toggleState
    }

    fun createCrew() = viewModelScope.launch {
        _createCrewState.value = CreateCrewState(isLoading = true)

        val crewReq = CreateCrewReq(
            ageList = _crewAgeGroupList.map { it + 1 },
            areaId = _crewDistrictId.value!! + 1,
            conference = if (_noCrewFee.value) null else _crewFee.value.wonToNum(),
            extraInfoList = _crewExtraInfoList.map { it + 1 },
            guestConference = if (_noCrewVisitorFee.value) null else _crewVisitorFee.value.wonToNum(),
            inquiries = _crewInquiry.value,
            materials = if (_crewSupplies.value.isNotEmpty()) _crewSupplies.value else null,
            introduction = _crewIntro.value,
            location = if (_crewLocation.value.isEmpty()) null else _crewLocation.value,
            sportsId = _crewSportId.value + 1,
            title = _crewTitle.value,
            memberCount = _crewSize.value.toInt(),
            scheduleList = _scheduleList.map {
                Schedule(
                    it.day.value + 1,
                    it.startTime.value.stringToDouble(),
                    it.endTime.value.stringToDouble()
                )
            }
        )
        val response = crewRepository.createCrew(crewReq)
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (res.isSuccess) postCrewImage(res.result.clubId)
                else _createCrewState.value = CreateCrewState(error = res.message)
            }
        } else _createCrewState.value = CreateCrewState(error = "크루 생성을 실패했습니다.")
    }

    private fun postCrewImage(crewId: Int) = viewModelScope.launch {
//        Log.d(
//            "{CreateCrewViewModel.postCrewImage}",
//            "${_isUsingDefaultImage.value}, ${_crewImage.value}"
//        )
//        if (_crewImage.value == EMPTY_IMAGE_URI) {
//            _createCrewState.value = CreateCrewState(data = crewId)
//        } else {
//            if (_crewImage.value.path != null) {
//
//                Log.d("{CreateCrewViewModel.postCrewImage}", "${File(_crewImage.value.path!!)}")
//                val response =
//                    crewRepository.postCrewImage(File(_crewImage.value.path!!).toString(), crewId)
//                if (response.isSuccessful) {
//                    response.body()?.let { res ->
//                        if (res.isSuccess) _createCrewState.value = CreateCrewState(data = crewId)
//                        else _createCrewState.value = CreateCrewState(error = res.message)
//                    }
//                } else _createCrewState.value = CreateCrewState(error = "크루 이미지 업로드를 실패했습니다.")
//            } else {
//                _createCrewState.value = CreateCrewState(error = "크루 이미지 업로드를 실패했습니다.")
//            }
//        }
    }

    fun testCrewImage(uri: Uri) = viewModelScope.launch {

//        val stream = context.contentResolver.openInputStream(uri)
//        Log.d("{CreateCrewViewModel.testCrewImage}", stream.toString())

        val path = getRealPathFromURIForGallery(uri)
        Log.d("{CreateCrewViewModel.testCrewImage}", path.toString())

        if (path != null) {

            val file = getImageBody("image", File(path))
            Log.d("{CreateCrewImageFragment.IntroImageLayout}", file.toString())

            val response = crewRepository.postCrewImage(file, 31)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _createCrewState.value = CreateCrewState(data = 31)
                    else _createCrewState.value = CreateCrewState(error = res.message)
                }
            } else _createCrewState.value = CreateCrewState(error = "크루 이미지 업로드를 실패했습니다.")
        }

    }

    fun getImageBody(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
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
            var document_id = cursor.getString(0)
            if (document_id == null) {
                for (i in 0 until cursor.columnCount) {
                    if (column.equals(cursor.getColumnName(i), ignoreCase = true)) {
                        fullPath = cursor.getString(i)
                        break
                    }
                }
            } else {
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
                cursor.close()
                val projection = arrayOf(column)
                try {
                    cursor = context.contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        MediaStore.Images.Media._ID + " = ? ",
                        arrayOf(document_id),
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

//        // Open a specific media item using ParcelFileDescriptor.
//        val resolver = context.contentResolver
//
//// "rw" for read-and-write;
//// "rwt" for truncating or overwriting existing file contents.
//        val readOnlyMode = "r"
//        resolver.openFileDescriptor(uri, readOnlyMode).use { file ->
//            // Perform operations on "pfd".
//            Log.d("{CreateCrewViewModel.getRealPathFromURIForGallery}", file.toString())
//        }
//        return null

//        val projection = arrayOf(MediaStore.Images.Media._ID)
////        val selection = sql-where-clause-with-placeholder-variables
////        val selectionArgs = values-of-placeholder-variables
////        val sortOrder = sql-order-by-clause
//
//        context.contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            projection, null, null, null
//        )?.use { cursor ->
//            while (cursor.moveToNext()) {
//                // Use an ID column from the projection to get
//                // a URI representing the media item itself.
//                val columnIdx: Int = cursor.getColumnIndex(MediaStore.Images.Media._ID)
//                return cursor.getString(columnIdx)
//            }
//        }
//        return null

//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor: Cursor? = context.contentResolver.query(
//            uri, projection, null,
//            null, null
//        )
//        if (cursor != null) {
//            cursor.moveToFirst()
//            val columnIdx: Int = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
//            return cursor.getString(columnIdx)
//
//        }
//        cursor?.close()
//        return uri.path

//        if (uri?.path?.startsWith("/storage")) {
//            return contentUri.getPath();
//        }
//        val id = DocumentsContract.getDocumentId(uri).split(":")[1]
//        val columns = arrayOf(MediaStore.Images.Media._ID)
//        val selection = MediaStore.Images.Media._ID + " = " + id
//        val cursor: Cursor? = context.contentResolver.query(
//            uri, columns, selection,
//            null, null
//        )
//        if (cursor != null) {
//
//            try {
//                val columnIndex = cursor.getColumnIndex(columns[0])
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(columnIndex)
//                }
//            } finally {
//                cursor.close()
//            }
//
//        }
//        return null

//        val filePath: String
//        val filePathColumn = arrayOf(MediaColumns._ID)
//
//        val cursor: Cursor? =
//            context.contentResolver.query(uri, null, null, null, null)
//        cursor?.moveToFirst()
//
//        val path = cursor?.getString(cursor.getColumnIndex("_data") ?: 0)
////        filePath = cursor!!.getString(columnIndex)
//        cursor?.close()
//        return path

    }
}