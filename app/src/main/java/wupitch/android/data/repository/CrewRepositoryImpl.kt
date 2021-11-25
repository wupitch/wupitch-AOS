package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CrewApi
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

class CrewRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : CrewRepository{

    override suspend fun createCrew(crewReq: CreateCrewReq): Response<CreateCrewRes> =
        retrofit.create(CrewApi::class.java).createCrew(crewReq)

    override suspend fun postCrewImage(images: RequestBody, file : MultipartBody.Part, crewId: Int): Response<BaseRes> =
        retrofit.create(CrewApi::class.java).postCrewImage(crewId, file, images)

}