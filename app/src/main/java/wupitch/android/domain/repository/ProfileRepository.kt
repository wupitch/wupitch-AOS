package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import wupitch.android.data.remote.dto.*

interface ProfileRepository {
    suspend fun getUserInfo() : Response<UserInfoRes>
    suspend fun changePw(newPw : PwReq) : Response<BaseRes>
    suspend fun checkPwMatch(pw : PwReq) : Response<BaseRes>
    suspend fun changeNotiStatus() : Response<BaseRes>
    suspend fun unregisterUser() : Response<BaseRes>
    suspend fun postProfileImage(images: RequestBody, file : MultipartBody.Part) : Response<BaseRes>
    suspend fun deleteProfileImage() : Response<BaseRes>
    suspend fun updateUserInfo(userInfoReq : UpdateUserInfoReq) : Response<BaseRes>
    suspend fun getUserDistrict() : Response<UserDistrictRes>
    suspend fun getUserSports() : Response<UserSportsRes>
    suspend fun getUserAgeGroup() : Response<UserAgeGroupRes>
    suspend fun getUserPhoneNum() : Response<UserPhoneNumRes>
}