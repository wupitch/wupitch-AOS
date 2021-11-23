package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.domain.model.CreateCrewReq

interface CrewApi {
    @POST("app/clubs")
    suspend fun createCrew(
        @Body createCrewReq : CreateCrewReq
    ) : Response<BaseRes>
}