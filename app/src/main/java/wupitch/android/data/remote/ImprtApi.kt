package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.*
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

    @GET("app/impromptus")
    suspend fun getImprts(
        @Query("areaId") areaId : Int?,
        @Query("days") days : List<Int>?,
        @Query("memberCountIndex") memberCountIndex : Int?,
        @Query("page") page : Int,
        @Query("scheduleIndex") scheduleIndex : Int?,
    ) : Response<GetImprtRes>

    @PATCH("app/impromptus/{impromptuId}/pinUp-toggle")
    suspend fun changePinStatus(
        @Path("impromptuId") impromptuId : Int
    ): Response<BaseResultRes>

    @POST("app/impromptus/{impromptuId}/participation-toggle")
    suspend fun participateImprt(
        @Path("impromptuId") impromptuId : Int
    ): Response<BaseResultRes>

    @GET("app/impromptus/title")
    suspend fun getImprtSearch(
        @Query("title") title : String,
        @Query("page") page : Int,
    ): Response<GetImprtRes>

    @GET("app/impromptus/accounts/auth")
    suspend fun getMyImpromptus() : Response<GetMyImprt>
}