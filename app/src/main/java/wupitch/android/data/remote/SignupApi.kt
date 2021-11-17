package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.SignupRes
import wupitch.android.domain.model.SignupReq

interface SignupApi {

    @POST("app/sign-up")
    suspend fun postSignup(
        @Body signup : SignupReq
    ) : Response<SignupRes>
}