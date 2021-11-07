package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import wupitch.android.data.remote.GetDistrictApi
import wupitch.android.data.remote.GetSportApi
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.data.remote.dto.SportRes
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import javax.inject.Inject

class GetSportRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : GetSportRepository {

    override suspend fun getSport(): Response<SportRes> =
        retrofit.create(GetSportApi::class.java).getSports()

}