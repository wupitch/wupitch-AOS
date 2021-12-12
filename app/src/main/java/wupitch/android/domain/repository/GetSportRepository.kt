package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.response.SportRes

interface GetSportRepository {
    suspend fun getSport() : Response<SportRes>
}