package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.PwReq
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.data.remote.dto.UserInfoRes

interface ProfileRepository {
    suspend fun getUserInfo() : Response<UserInfoRes>
    suspend fun changePw(newPw : PwReq) : Response<BaseRes>
    suspend fun checkPwMatch(pw : PwReq) : Response<BaseRes>
    suspend fun changeNotiStatus() : Response<BaseRes>
    suspend fun unregisterUser() : Response<BaseRes>
    suspend fun postProfileImage(images: RequestBody, file : MultipartBody.Part) : Response<BaseRes>
    suspend fun updateUserInfo(userInfoReq : UpdateUserInfoReq) : Response<BaseRes>
}