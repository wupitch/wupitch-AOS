package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CrewApi
import wupitch.android.data.remote.dto.*
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

class CrewRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : CrewRepository{

    override suspend fun createCrew(crewReq: CreateCrewReq): Response<CreateCrewRes> =
        retrofit.create(CrewApi::class.java).postCrew(crewReq)

    override suspend fun postCrewImage(images: RequestBody, file : MultipartBody.Part, crewId: Int): Response<BaseRes> =
        retrofit.create(CrewApi::class.java).postCrewImage(crewId, file, images)

    override suspend fun getCrewDetail(crewId: Int): Response<CrewDetailRes>
    = retrofit.create(CrewApi::class.java).getCrewDetail(crewId)

    override suspend fun getCrew(
        ageList: List<Int>?,
        areaId: Int?,
        days: List<Int>?,
        memberCountValue: Int?,
        page: Int,
        sportsList: List<Int>?
    ) = retrofit.create(CrewApi::class.java).getCrews(ageList = ageList,
    areaId= areaId, days = days, memberCountValue = memberCountValue, page = page, sportsList = sportsList)

    override suspend fun changePinStatus(id : Int): Response<BaseResultRes>
    = retrofit.create(CrewApi::class.java).changePinStatus(id)

    override suspend fun joinCrew(id: Int): Response<BaseResultRes>
    = retrofit.create(CrewApi::class.java).participateCrew(id)

    override suspend fun getCrewSearch(crewTitle: String, page : Int): Response<GetCrewRes>
    = retrofit.create(CrewApi::class.java).getCrewSearch(crewTitle, page)

    override suspend fun getCrewVisitorDates(crewId: Int): Response<GetCrewVisitorDates>
    = retrofit.create(CrewApi::class.java).getCrewVisitorDates(crewId)

    override suspend fun postVisit(visitReq: CrewVisitorReq): Response<BaseRes>
    = retrofit.create(CrewApi::class.java).postVisit(visitReq)

    override suspend fun getMyCrews(): Response<GetMyCrewRes>
    = retrofit.create(CrewApi::class.java).getMyCrews()


}