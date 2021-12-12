package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.response.DistrictRes

interface GetDistrictRepository {
    suspend fun getDistricts() : Response<DistrictRes>
}