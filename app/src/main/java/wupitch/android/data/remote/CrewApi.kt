package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.data.remote.dto.CrewDetailRes
import wupitch.android.domain.model.CreateCrewReq

interface CrewApi {
    @POST("app/clubs")
    suspend fun postCrew(
        @Body createCrewReq : CreateCrewReq
    ) : Response<CreateCrewRes>

    @Multipart
    @PATCH("app/clubs/image")
    suspend fun postCrewImage(
        @Query ("crewId") crewId: Int,
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>

    @GET("app/clubs/{clubId}")
    suspend fun getCrewDetail(
        @Path("clubId") crewId : Int
    ) : Response<CrewDetailRes>
}