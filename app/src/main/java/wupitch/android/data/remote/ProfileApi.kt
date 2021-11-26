package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.ChangePwReq
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.data.remote.dto.UserInfoRes

interface ProfileApi {

    @GET("app/accounts/auth")
    suspend fun getUserInfo() : Response<UserInfoRes>

    @Multipart
    @POST("app/accounts/image")
    suspend fun postProfileImage(
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>

    @PATCH("app/accounts/auth/password")
    suspend fun patchPw(
        @Body newPw : ChangePwReq
    ) : Response<BaseRes>

    @PATCH("app/accounts/toggle-alarm-info")
    suspend fun patchNotiStatus() : Response<BaseRes>

    @PATCH("app/accounts/toggle-status")
    suspend fun patchUnregister() : Response<BaseRes>

    @PATCH("app/accounts/information")
    suspend fun updateUserInfo(
        @Body userInfoReq : UpdateUserInfoReq
    ) : Response<BaseRes>

}