package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.GET
import wupitch.android.data.remote.response.SportRes

interface GetSportApi {
    @GET("app/sports")
    suspend fun getSports() : Response<SportRes>
}