package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.*

interface ProfileApi {

    @GET("app/accounts/auth")
    suspend fun getUserInfo() : Response<UserInfoRes>

    @Multipart
    @POST("app/accounts/image")
    suspend fun postProfileImage(
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>

    @PATCH("app/accounts/image/empty")
    suspend fun deleteProfileImage() : Response<BaseRes>

    @PATCH("app/accounts/auth/password")
    suspend fun patchPw(
        @Body newPw : PwReq
    ) : Response<BaseRes>

    @PATCH("app/accounts/toggle-alarm-info")
    suspend fun patchNotiStatus() : Response<BaseRes>

    @PATCH("app/accounts/toggle-status")
    suspend fun patchUnregister() : Response<BaseRes>

    @PATCH("app/accounts/information")
    suspend fun updateUserInfo(
        @Body userInfoReq : UpdateUserInfoReq
    ) : Response<BaseRes>

    @POST("app/accounts/auth/password/check")
    suspend fun checkPwMatch(
        @Body pw : PwReq
    ): Response<BaseRes>

    @GET("app/accounts/auth/area")
    suspend fun getUserDistrict() : Response<UserDistrictRes>

    @GET("app/accounts/auth/age")
    suspend fun getUserAgeGroup() : Response<UserAgeGroupRes>

    @GET("app/accounts/auth/phoneNumber")
    suspend fun getUserPhoneNumber() : Response<UserPhoneNumRes>

    @GET("app/accounts/auth/sports")
    suspend fun getUserSport() : Response<UserSportsRes>

}