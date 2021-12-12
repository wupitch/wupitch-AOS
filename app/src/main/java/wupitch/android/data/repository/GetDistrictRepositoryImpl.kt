package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.GetDistrictApi
import wupitch.android.data.remote.response.DistrictRes
import wupitch.android.domain.repository.GetDistrictRepository
import javax.inject.Inject

class GetDistrictRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : GetDistrictRepository {

    override suspend fun getDistricts(): Response<DistrictRes> =
        retrofit.create(GetDistrictApi::class.java).getDistricts()

}