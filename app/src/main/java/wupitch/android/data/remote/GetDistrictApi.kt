package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.GET
import wupitch.android.data.remote.response.DistrictRes

interface GetDistrictApi {

    @GET("app/areas")
    suspend fun getDistricts() : Response<DistrictRes>

}