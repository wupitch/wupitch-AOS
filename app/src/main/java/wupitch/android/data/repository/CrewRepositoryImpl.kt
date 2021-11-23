package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CrewApi
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

class CrewRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : CrewRepository{

    override suspend fun createCrew(crewReq: CreateCrewReq): Response<BaseRes> =
        retrofit.create(CrewApi::class.java).createCrew(crewReq)

}