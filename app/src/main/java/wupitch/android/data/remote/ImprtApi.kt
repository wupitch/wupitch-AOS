package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.CreateImprtRes
import wupitch.android.domain.model.CreateImprtReq

interface ImprtApi {
    @POST("app/impromptus")
    suspend fun postImpromptu(
        @Body imprtReq : CreateImprtReq
    ) : Response<CreateImprtRes>
}