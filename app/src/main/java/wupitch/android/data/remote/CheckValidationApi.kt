package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.ValidReq
import wupitch.android.data.remote.dto.ValidationRes

interface CheckValidationApi {
    @POST("app/accounts/nickname/validation")
    suspend fun checkNicknameValid (
        @Body nickname : ValidReq
    ) : Response<ValidationRes>
}