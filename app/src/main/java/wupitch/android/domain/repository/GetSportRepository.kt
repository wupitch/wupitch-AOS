package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.DistrictRes
import wupitch.android.data.remote.dto.SportRes

interface GetSportRepository {
    suspend fun getSport() : Response<SportRes>
}