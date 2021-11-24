package wupitch.android.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.domain.model.CreateCrewReq

interface CrewApi {
    @POST("app/clubs")
    suspend fun createCrew(
        @Body createCrewReq : CreateCrewReq
    ) : Response<CreateCrewRes>

    @Multipart
    @PATCH("app/clubs/image")
    suspend fun postCrewImage(
        @Query ("crewId") crewId: Int,
        @Part images : MultipartBody.Part
    ) : Response<BaseRes>
}