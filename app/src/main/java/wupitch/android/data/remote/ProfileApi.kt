package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.ChangePwReq
import wupitch.android.data.remote.dto.UserInfoRes

interface ProfileApi {

    @GET("app/accounts/auth")
    suspend fun getUserInfo() : Response<UserInfoRes>

    @PATCH("app/accounts/auth/password")
    suspend fun patchPw(
        @Body newPw : ChangePwReq
    ) : Response<BaseRes>

    @PATCH("app/accounts/toggle-alarm-info")
    suspend fun patchNotiStatus() : Response<BaseRes>

    @PATCH("app/accounts/toggle-status")
    suspend fun patchUnregister() : Response<BaseRes>

}