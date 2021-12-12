package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.response.BaseRes
import wupitch.android.data.remote.response.SignupRes
import wupitch.android.domain.model.SignupReq

interface SignupApi {

    @POST("app/sign-up")
    suspend fun postSignup(
        @Body signup : SignupReq
    ) : Response<SignupRes>

    @Multipart
    @POST("app/accounts/identification")
    suspend fun postIdCardImage(
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>
}