package wupitch.android.presentation.ui.main.my_page

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wupitch.android.CrewFilter
import com.wupitch.android.ImpromptuFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.data.remote.dto.*
import wupitch.android.domain.repository.ProfileRepository
import wupitch.android.util.GetRealPath
import wupitch.android.util.getImageBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userInfoDataStore : DataStore<Preferences>,
    private val crewFilterDataStore : DataStore<CrewFilter>,
    private val imprtFilterDataStore : DataStore<ImpromptuFilter>,
    private val getRealPath: GetRealPath
) : ViewModel() {

    /*
    * user info
    * */
    private var _userInfo = mutableStateOf(UserInfoState())
    val userInfo: State<UserInfoState> = _userInfo


    fun getUserInfo() = viewModelScope.launch {
        _userInfo.value = UserInfoState(isLoading = true)

        val response = profileRepository.getUserInfo()
        if (response.isSuccessful) {
            response.body()?.let { infoRes ->
                if (infoRes.isSuccess) _userInfo.value =
                    UserInfoState(data = infoRes.result.toResult())
                else _userInfo.value = UserInfoState(error = infoRes.message)
            }
        } else _userInfo.value = UserInfoState(error = "유저 정보 조회를 실패했습니다.")
    }

    /**
     * snack bar
     */
    private var _notEnoughInfo = mutableStateOf(false)
    val notEnoughInfo: State<Boolean> = _notEnoughInfo

    fun checkNotEnoughInfo() = viewModelScope.launch {
        val prefFlow = userInfoDataStore.data.first()
        prefFlow[Constants.FIRST_COMER]?.let {
            if (it) {
                _notEnoughInfo.value = true
            }
        }
    }

    fun setNotEnoughInfo() = viewModelScope.launch {
        delay(2200L)
        _notEnoughInfo.value = false
        userInfoDataStore.edit { settings ->
            settings[Constants.FIRST_COMER] = false
        }
    }

    /*
    * update state
    * */
    private var _updateState = mutableStateOf(BaseState())
    val updateState: State<BaseState> = _updateState


    /*
    * password
    * */
    private var _isCurrentPwValid = mutableStateOf<Boolean?>(null)
    val isCurrentPwValid: State<Boolean?> = _isCurrentPwValid

    private var _isNewPwValid = mutableStateOf<Boolean?>(null)
    val isNewPwValid: State<Boolean?> = _isNewPwValid

    private var _userNewPw = mutableStateOf("")

    private var _changePwState = mutableStateOf(BaseState())
    val changePwState: State<BaseState> = _changePwState

    /*
    *
    * notification
    * */
    private var _userNotiState = mutableStateOf(NotiState())
    val userNotiState: State<NotiState> = _userNotiState


    fun setUserNotiState(value: Boolean) {
        _userNotiState.value = NotiState(data = value)
    }

    fun changeUserNotiState() = viewModelScope.launch {
        _userNotiState.value = NotiState(isLoading = true)

        val response = profileRepository.changeNotiStatus()
        if (response.isSuccessful) {
            response.body()?.let { baseRes ->
                if (!baseRes.isSuccess) _userNotiState.value = NotiState(error = baseRes.message)
            }
        } else _userNotiState.value = NotiState(error = "푸시 알림 상태 변경에 실패했습니다.")
    }


    /*
    * logout and unregister
    * */
    fun logoutUser() = viewModelScope.launch {

        userInfoDataStore.edit { settings ->
            settings[Constants.JWT_PREFERENCE_KEY] = ""
            settings[Constants.USER_ID] = -1
            settings[Constants.USER_NICKNAME] = ""
        }
    }

    private var _unregisterState = mutableStateOf(BaseState())
    val unregisterState: State<BaseState> = _unregisterState

    fun unregisterUser() = viewModelScope.launch {

        _unregisterState.value = BaseState(isLoading = true)

        val response = profileRepository.unregisterUser()
        if (response.isSuccessful) {
            response.body()?.let { baseRes ->
                if (baseRes.isSuccess) {
                    _unregisterState.value = BaseState(isSuccess = true)
                     userInfoDataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = ""
                        settings[Constants.USER_ID] = -1
                        settings[Constants.USER_NICKNAME] = ""
                        settings[Constants.IS_CONFIRMED] = false
                    }
                    crewFilterDataStore.updateData {
                        it.toBuilder()
                            .clearAgeList()
                            .clearAreaId()
                            .clearAreaName()
                            .clearDayList()
                            .clearSize()
                            .clearSportList()
                            .build()
                    }
                    imprtFilterDataStore.updateData {
                        it.toBuilder()
                            .clearAreaId()
                            .clearAreaName()
                            .clearDays()
                            .clearRecruitSize()
                            .clearSchedule()
                            .build()
                    }
                } else _unregisterState.value = BaseState(error = baseRes.message)
            }
        } else _unregisterState.value = BaseState(error = "회원 탈퇴를 실패했습니다.")
    }


    /*
    * password
    * */
    fun checkCurrentPwValid(pw: String) = viewModelScope.launch {
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

    fun checkNewPwValid(pw: String) {
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
        if (response.isSuccessful) {
            response.body()?.let { baseRes ->
                if (baseRes.isSuccess) _changePwState.value = BaseState(isSuccess = true)
                else _changePwState.value = BaseState(error = baseRes.message)
            }
        } else _changePwState.value = BaseState(error = "비밀번호 변경을 실패했습니다.")
    }

    /*
    * Image
    * */

    private var _isUsingDefaultImage = mutableStateOf<Boolean?>(null)
    val isUsingDefaultImage: State<Boolean?> = _isUsingDefaultImage

    private var _userImageState = mutableStateOf<Uri>(Constants.EMPTY_IMAGE_URI)
    val userImageState: State<Uri> = _userImageState

    private var _imageChosenState = mutableStateOf(false)
    val imageChosenState: State<Boolean> = _imageChosenState

    fun setIsUsingDefaultImage(isUsingDefaultImage: Boolean?) {
        _isUsingDefaultImage.value = isUsingDefaultImage
        if(isUsingDefaultImage == true)  {
            _userImageState.value = Constants.EMPTY_IMAGE_URI
            deleteUserImage()
        }

    }
    fun setImageChosenState(isImageChosen: Boolean) {
        _imageChosenState.value = isImageChosen
    }

    private var _uploadImageState = mutableStateOf(BaseState())
    val uploadImageState: State<BaseState> = _uploadImageState

    private fun deleteUserImage() = viewModelScope.launch {
        _uploadImageState.value = BaseState(isLoading = true)

        val response = profileRepository.deleteProfileImage()
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) _uploadImageState.value = BaseState(isSuccess = true)
                else _uploadImageState.value = BaseState(error = res.message)
            }
        }else _uploadImageState.value = BaseState(error = "이미지 변경에 실패했습니다.")
    }

    fun uploadUserImage(uri: Uri) = viewModelScope.launch {
        _uploadImageState.value = BaseState(isLoading = true)

        _userImageState.value = uri
        val path = getRealPath.getRealPathFromURIForGallery(uri)

        if (path != null) {
            resizeImage(file = File(path))

            val file = getImageBody(File(path))

            val response = profileRepository.postProfileImage(file.body, file)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _uploadImageState.value = BaseState(isSuccess = true)
                    else _uploadImageState.value = BaseState(error = res.message)
                }
            } else _uploadImageState.value = BaseState(error = "프로필 이미지 업로드를 실패했습니다.")
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

}