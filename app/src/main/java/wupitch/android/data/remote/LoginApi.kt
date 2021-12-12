package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.response.LoginRes
import wupitch.android.domain.model.LoginReq

interface LoginApi {
    @POST("app/sign-in")
    suspend fun postLogin(
        @Body loginReq : LoginReq
    ) : Response<LoginRes>
}