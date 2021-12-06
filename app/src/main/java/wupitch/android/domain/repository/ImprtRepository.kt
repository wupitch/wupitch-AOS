package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.*
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.CreateImprtReq

interface ImprtRepository {

    suspend fun createImprt(imprtReq : CreateImprtReq) : Response<CreateImprtRes>
    suspend fun postImprtImage(images: RequestBody, file : MultipartBody.Part, impromptuId : Int) : Response<BaseRes>
    suspend fun getImprtDetail(id : Int) : Response<ImprtDetailRes>
    suspend fun getImpromptu(
        areaId: Int?, days: List<Int>?,
        memberCountIndex: Int?,
        page: Int, scheduleIndex: Int?
    ): Response<GetImprtRes>
    suspend fun changePinStatus( id : Int) : Response<BaseResultRes>
    suspend fun joinImprt( id : Int) : Response<BaseResultRes>
    suspend fun getImprtFilter() : Response<GetImprtFilterRes>
    suspend fun getSearchImprt(title: String, page : Int) : Response<GetImprtRes>

}