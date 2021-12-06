package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.*
import wupitch.android.domain.model.CreateCrewReq

interface CrewRepository {

    suspend fun createCrew(crewReq: CreateCrewReq): Response<CreateCrewRes>
    suspend fun postCrewImage(
        images: RequestBody,
        file: MultipartBody.Part,
        crewId: Int
    ): Response<BaseRes>

    suspend fun getCrewDetail(crewId: Int): Response<CrewDetailRes>
    suspend fun getCrew(
        ageList: List<Int>?,
        areaId: Int?, days: List<Int>?, memberCountValue: Int?,
        page: Int, sportsList: List<Int>?
    ): Response<GetCrewRes>

    suspend fun changePinStatus(id : Int) : Response<BaseResultRes>
    suspend fun joinCrew(id : Int) : Response<BaseResultRes>
    suspend fun getCrewFilter() : Response<GetCrewFilterRes>
    suspend fun getCrewSearch(crewTitle: String, page : Int): Response<GetCrewRes>
    suspend fun getCrewVisitorDates(crewId : Int) : Response<GetCrewVisitorDates>
    suspend fun postVisit(visitReq : CrewVisitorReq) : Response<BaseRes>

}