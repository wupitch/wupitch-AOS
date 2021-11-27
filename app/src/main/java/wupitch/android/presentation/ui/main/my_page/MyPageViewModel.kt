package wupitch.android.presentation.ui.main.my_page

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.preferences.core.edit
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
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.*
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import wupitch.android.domain.repository.ProfileRepository
import wupitch.android.presentation.ui.main.home.create_crew.DistrictState
import wupitch.android.presentation.ui.main.home.create_crew.SportState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val getDistrictRepository: GetDistrictRepository,
    private val getSportRepository: GetSportRepository,
    private val profileRepository: ProfileRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _userInfo = mutableStateOf(UserInfoState())
    val userInfo : State<UserInfoState> = _userInfo

    private var _userNotiState = mutableStateOf(NotiState())
    val userNotiState : State<NotiState> = _userNotiState

    //profile
    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    val userNickname: LiveData<String?> = _userNickname

    private var _userIntroduce = MutableLiveData<String>()
    val userIntroduce: LiveData<String> = _userIntroduce

    //district
    private var _districtList = mutableStateOf(DistrictState())
    val districtList: State<DistrictState> = _districtList

    private var _userDistrictId = MutableLiveData<Int>()
    val userDistrictId: LiveData<Int> = _userDistrictId

    private var _userDistrictName = MutableLiveData<String>()
    val userDistrictName: LiveData<String> = _userDistrictName

    //sports
    private var _userSportList = mutableStateListOf<Int>()
    val userSportList: SnapshotStateList<Int> = _userSportList

    private var _sportsList = mutableStateOf(SportState())
    val sportsList: State<SportState> = _sportsList

    private var _userSportId = mutableStateOf(-1)
    val userSportId: State<Int> = _userSportId

    //age group
    private var _userAge = MutableLiveData<Int>()
    val userAge: LiveData<Int> = _userAge

    //contact
    var userPhoneNum = mutableStateOf("")

    //password
    private var _isCurrentPwValid = mutableStateOf<Boolean?>(null)
    val isCurrentPwValid : State<Boolean?> = _isCurrentPwValid

    private var _isNewPwValid = mutableStateOf<Boolean?>(null)
    val isNewPwValid : State<Boolean?> = _isNewPwValid

    private var _userNewPw = mutableStateOf("")

    private var _changePwState = mutableStateOf(BaseState())
    val changePwState : State<BaseState> = _changePwState

    fun getUserInfo() = viewModelScope.launch {
        _userInfo.value = UserInfoState(isLoading = true)

        val response = profileRepository.getUserInfo()
        if(response.isSuccessful){
            response.body()?.let { infoRes ->
                if(infoRes.isSuccess) _userInfo.value = UserInfoState(data = infoRes.result.toResult())
                else  _userInfo.value = UserInfoState(error = infoRes.message)
            }
        }else  _userInfo.value = UserInfoState(error = "유저 정보 조회를 실패했습니다.")
    }

    fun setUserNotiState(value : Boolean){
        _userNotiState.value = NotiState(data = value)
    }

    fun changeUserNotiState() = viewModelScope.launch {
        _userNotiState.value = NotiState(isLoading = true)

        val response = profileRepository.changeNotiStatus()
        if(response.isSuccessful){
            response.body()?.let { baseRes ->
                if(baseRes.isSuccess) _userNotiState.value = NotiState(data = !_userNotiState.value.data)
                else _userNotiState.value = NotiState(error = baseRes.message)
            }
        }else _userNotiState.value = NotiState(error = "푸시 알림 상태 변경에 실패했습니다.")
    }


    fun checkNicknameValid(nickname: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkNicknameValid}", nickname)
        if (nickname.isEmpty()) {
            _isNicknameValid.value = null
            return@launch
        }

        val response = checkValidRepository.checkNicknameValidation(NicknameValidReq(nickname))
        if (response.isSuccessful) {
            response.body()?.let { validRes ->
                if (validRes.isSuccess) {
                    _isNicknameValid.value = true
                    _userNickname.value = nickname
                } else {
                    _isNicknameValid.value = false
                }
            }
        } else _isNicknameValid.value = false

    }

    fun setUserIntroduce(introduce: String) {
        _userIntroduce.value = introduce
    }

    private var _updateState = mutableStateOf(BaseState())
    val updateState : State<BaseState> = _updateState

    fun changeUserNicknameOrIntro() = viewModelScope.launch {
        getUserInfo()
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            nickname = if(_userNickname.value == _userInfo.value.data.nickname) null else _userNickname.value,
            introduce = if(_userIntroduce.value == _userInfo.value.data.introduce) null else _userIntroduce.value
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!

    }

    //todo
    fun changeUserDistrict() = viewModelScope.launch {
        getUserInfo()
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            areaId = _userDistrictId.value!! + 1//if(_userDistrictId.value == _userInfo.value.data.) null else _userDistrictId.value + 1,
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!
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

    fun setUserDistrict(districtId: Int, districtName: String) {
        _userDistrictId.value = districtId
        _userDistrictName.value = districtName
        Log.d("{SignupViewModel.getUserRegion}", "id : $districtId name : $districtName")
    }

    //todo sport without etc and edit this.
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
                        if (_userSportId.value == index) {
                            filterItem.state.value = true
                        }
                    }
                } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")
            }
        } else _sportsList.value = SportState(error = "스포츠 가져오기를 실패했습니다.")

    }

    fun setUserSportList(list: SnapshotStateList<Int>) {
        _userSportList = list
    }

    //todo
    fun changeUserSport() = viewModelScope.launch {
        getUserInfo()
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            sportsList = _userSportList.map { it +1 }//if(_userDistrictId.value == _userInfo.value.data.) null else _userDistrictId.value + 1,
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!
    }

    fun setUserAge(ageCode: Int) {
        _userAge.value = ageCode
        Log.d("{SignupViewModel.setUserAge}", _userAge.value.toString())
    }

    //todo
    fun changeUserAgeGroup() = viewModelScope.launch {
        getUserInfo()
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            ageNum = _userAge.value!! + 1//if(_userDistrictId.value == _userInfo.value.data.) null else _userDistrictId.value + 1,
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!
    }


    fun setUserPhoneNum(phoneNum: String) {
        userPhoneNum.value = phoneNum
    }

    //todo
    fun changeUserPhoneNum() = viewModelScope.launch {
        getUserInfo()
        _updateState.value = BaseState(isLoading = true)
        val req = UpdateUserInfoReq(
            phoneNumber = userPhoneNum.value //if(_userDistrictId.value == _userInfo.value.data.) null else _userDistrictId.value + 1,
        )
        val response = profileRepository.updateUserInfo(req)
        if(response.isSuccessful){
            response.body()?.let {
                if(it.isSuccess) _updateState.value = BaseState(isSuccess = true)
                else _updateState.value = BaseState(error = it.message)
            }
        }else _updateState.value = BaseState(error = "update failed") //todo to korean!!!
    }

    fun logoutUser() = viewModelScope.launch {

        context.dataStore.edit { settings ->
            settings[Constants.JWT_PREFERENCE_KEY] = ""
            settings[Constants.USER_ID] = -1
            settings[Constants.USER_NICKNAME] = ""
        }
    }

    private var _unregisterState = mutableStateOf(BaseState())
    val unregisterState : State<BaseState> = _unregisterState

    fun unregisterUser() = viewModelScope.launch {

        _unregisterState.value = BaseState(isLoading = true)

        val response = profileRepository.unregisterUser()
        if(response.isSuccessful){
            response.body()?.let { baseRes ->
                if(baseRes.isSuccess) {
                    _unregisterState.value = BaseState(isSuccess = true)
                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = ""
                        settings[Constants.USER_ID] = -1
                        settings[Constants.USER_NICKNAME] = ""
                    }
                }
                else _unregisterState.value = BaseState(error = baseRes.message)
            }
        }else _unregisterState.value = BaseState(error = "회원 탈퇴를 실패했습니다.")
    }

    fun checkCurrentPwValid(pw : String) = viewModelScope.launch {
        if (pw.isEmpty()) {
            _isCurrentPwValid.value = null
            return@launch
        }

        val response = profileRepository.checkPwMatch(PwReq(pw))
        if (response.isSuccessful) {
            response.body()?.let { baseRes ->
                if (baseRes.isSuccess) {
                    _isCurrentPwValid.value = baseRes.result
                } else {
                    _isCurrentPwValid.value = baseRes.result
                }
            }
        } else _isCurrentPwValid.value = false
    }

    fun checkNewPwValid(pw : String) {
        if (pw.isEmpty()) {
            _isNewPwValid.value = null
            return
        }
        val isPwValid = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$".toRegex())
        if (isPwValid) {
            _isNewPwValid.value = true
            _userNewPw.value = pw
        } else {
            _isNewPwValid.value = false
        }
    }

    fun changePw() = viewModelScope.launch {
        _changePwState.value = BaseState(isLoading = true)
        val response = profileRepository.changePw(PwReq(password = _userNewPw.value))
        if(response.isSuccessful){
            response.body()?.let { baseRes ->
                if(baseRes.isSuccess)  _changePwState.value = BaseState(isSuccess = true)
                else  _changePwState.value = BaseState(error = baseRes.message)
            }
        }else  _changePwState.value = BaseState(error = "비밀번호 변경을 실패했습니다.")
    }

    private var _userImageState = mutableStateOf(BaseState())
    val userImageState : State<BaseState> = _userImageState

    fun setUserImage(uri : Uri) = viewModelScope.launch {
        val path = getRealPathFromURIForGallery(uri)

        if (path != null) {
            resizeImage(file = File(path))

            val file = getImageBody(File(path))

            val response = profileRepository.postProfileImage( file.body, file)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _userImageState.value = BaseState(isSuccess = true)
                    else  _userImageState.value = BaseState(error = res.message)
                }
            } else  _userImageState.value = BaseState(error = "프로필 이미지 업로드를 실패했습니다.")
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