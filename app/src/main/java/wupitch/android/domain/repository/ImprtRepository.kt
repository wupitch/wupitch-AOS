package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import wupitch.android.data.remote.dto.*
import wupitch.android.data.remote.response.*
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
    suspend fun getSearchImprt(title: String, page : Int) : Response<GetImprtRes>
    suspend fun getMyImprt() : Response<GetMyImprt>
    suspend fun getImprtMembers(imprtId : Int) : Response<GetImprtMembersRes>
}