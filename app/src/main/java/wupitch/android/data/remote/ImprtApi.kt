package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateImprtRes
import wupitch.android.data.remote.dto.CrewDetailRes
import wupitch.android.data.remote.dto.ImprtDetailRes
import wupitch.android.domain.model.CreateImprtReq

interface ImprtApi {
    @POST("app/impromptus")
    suspend fun postImpromptu(
        @Body imprtReq : CreateImprtReq
    ) : Response<CreateImprtRes>

    @Multipart
    @PATCH("app/impromptu/image")
    suspend fun postImprtImage(
        @Query("impromptuId") impromptuId : Int,
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>

    @GET("app/impromptus/{impromptuId}")
    suspend fun getImprtDetail(
        @Path("impromptuId") impromptuId : Int
    ) : Response<ImprtDetailRes>
}