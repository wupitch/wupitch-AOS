package wupitch.android.presentation.ui.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import wupitch.android.common.BaseState
import wupitch.android.common.Constants
import wupitch.android.common.Constants.dataStore
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.SignupResult
import wupitch.android.domain.model.SignupReq
import wupitch.android.domain.repository.CheckValidRepository
import wupitch.android.domain.repository.SignupRepository
import wupitch.android.presentation.ui.main.home.create_crew.CreateCrewState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val checkValidRepository: CheckValidRepository,
    private val signupRepository: SignupRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _allToggleState = mutableStateOf<Boolean>(false)
    val allToggleState: State<Boolean> = _allToggleState

    private var _serviceToggleState = mutableStateOf<Boolean>(false)
    val serviceToggleState: State<Boolean> = _serviceToggleState

    private var _privacyToggleState = mutableStateOf<Boolean>(false)
    val privacyToggleState: State<Boolean> = _privacyToggleState

    private var _pushToggleState = MutableLiveData<Boolean>()
    val pushToggleState: LiveData<Boolean> = _pushToggleState


    private var _isNicknameValid = MutableLiveData<Boolean?>()
    val isNicknameValid: LiveData<Boolean?> = _isNicknameValid

    private var _userNickname = MutableLiveData<String?>()
    val userNickname: LiveData<String?> = _userNickname

    private var _userIntroduce = MutableLiveData<String>()
    val userIntroduce: LiveData<String> = _userIntroduce

    private var _isEmailValid = MutableLiveData<Boolean?>()
    val isEmailValid: LiveData<Boolean?> = _isEmailValid

    private var _userEmail = MutableLiveData<String?>()
    val userEmail: LiveData<String?> = _userEmail

    private var _isPwValid = MutableLiveData<Boolean?>()
    val isPwValid: LiveData<Boolean?> = _isPwValid

    private var _userPw = MutableLiveData<String?>()
    val userPw: LiveData<String?> = _userPw

    private var _signupState = mutableStateOf(BaseState())
    val signupState: State<BaseState> = _signupState

    fun setAllToggleState(state: Boolean) {
        _allToggleState.value = state
    }

    fun setServiceToggleState(state: Boolean) {
        _serviceToggleState.value = state
    }

    fun setPrivacyToggleState(state: Boolean) {
        _privacyToggleState.value = state
    }

    fun setPushToggleState(state: Boolean) {
        _pushToggleState.value = state
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

    fun checkEmailValid(email: String) = viewModelScope.launch {
        Log.d("{SignupViewModel.checkEmailValid}", email)
        if (email.isEmpty()) {
            _isEmailValid.value = null
            return@launch
        }

        val response = checkValidRepository.checkEmailValidation(EmailValidReq(email))
        if (response.isSuccessful) {
            response.body()?.let { validRes ->
                if (validRes.isSuccess) {
                    _isEmailValid.value = true
                    _userEmail.value = email
                } else {
                    _isEmailValid.value = false
                }
            }
        } else _isEmailValid.value = false
    }


    fun checkPwValid(pw: String) {

        if (pw.isEmpty()) {
            _isPwValid.value = null
            return
        }
        val isPwValid = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$".toRegex())
        if (isPwValid) {
            _isPwValid.value = true
            _userPw.value = pw
        } else {
            _isPwValid.value = false
        }

    }

    fun postSignup() = viewModelScope.launch {

        _signupState.value = BaseState(isLoading = true)

        val signupReq = SignupReq(
            email = _userEmail.value!!,
            introduce = _userIntroduce.value!!,
            isPushAgree = _pushToggleState.value!!,
            nickname = _userNickname.value!!,
            password = _userPw.value!!
        )
        val response = signupRepository.signup(signupReq)

        if (response.isSuccessful) {
            response.body()?.let { signupRes ->
                if (signupRes.isSuccess) {

                    //todo jwt 기준으로 신분증 사진이 들어간다는게 말이 되나? 신분증 인증 안 해도 일단 회원이 된다는게?
                    context.dataStore.edit { settings ->
                        settings[Constants.JWT_PREFERENCE_KEY] = signupRes.result.jwt
                        settings[Constants.USER_ID] = signupRes.result.accountId
                        settings[Constants.USER_NICKNAME] = signupRes.result.nickname
                    }
                    postIdCardImage()
                }
                else _signupState.value = BaseState(error = signupRes.message)
            }
        } else _signupState.value = BaseState(error = "회원가입에 실패했습니다.")
    }

    private var _idCardImage = mutableStateOf<Uri>(Constants.EMPTY_IMAGE_URI)
    val idCardImage: State<Uri> = _idCardImage

    fun setIdCardImage(uri : Uri) {
        _idCardImage.value = uri
    }

    private fun postIdCardImage() = viewModelScope.launch {

//        val path = getRealPathFromURIForGallery(context, _idCardImage.value)

//        if (path != null) {
//            resizeImage(file = File(path))

//            val file = getImageBody(File(path))
        if(_idCardImage.value.path != null){
            val file = getImageBody(File(_idCardImage.value.path!!))

            val response = signupRepository.postIdCardImage(images = file.body, file = file)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.isSuccess) _signupState.value = BaseState(isSuccess = true)
                    else  _signupState.value = BaseState(error = res.message)
                }
            } else _signupState.value = BaseState(error = "회원가입에 실패했습니다.")
        }else _signupState.value = BaseState(error = "회원가입에 실패했습니다.")
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

//    private fun getRealPathFromURIForGallery(uri: Uri): String? {
//
//        var fullPath: String? = null
//        val column = "_data"
//        var cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
//        if (cursor != null) {
//            cursor.moveToFirst()
//            var documentId = cursor.getString(0)
//            if (documentId == null) {
//                for (i in 0 until cursor.columnCount) {
//                    if (column.equals(cursor.getColumnName(i), ignoreCase = true)) {
//                        fullPath = cursor.getString(i)
//                        break
//                    }
//                }
//            } else {
//                documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
//                cursor.close()
//                val projection = arrayOf(column)
//                try {
//                    cursor = context.contentResolver.query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        projection,
//                        MediaStore.Images.Media._ID + " = ? ",
//                        arrayOf(documentId),
//                        null
//                    )
//                    if (cursor != null) {
//                        cursor.moveToFirst()
//                        fullPath = cursor.getString(cursor.getColumnIndexOrThrow(column))
//                    }
//                } finally {
//                    if (cursor != null) cursor.close()
//                }
//            }
//        }
//        return fullPath
//    }


}