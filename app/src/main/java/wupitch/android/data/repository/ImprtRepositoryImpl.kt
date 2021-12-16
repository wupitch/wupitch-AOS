package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.ImprtApi
import wupitch.android.data.remote.dto.*
import wupitch.android.data.remote.response.*
import wupitch.android.domain.model.CreateImprtReq
import wupitch.android.domain.repository.ImprtRepository
import javax.inject.Inject

class ImprtRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : ImprtRepository {

    override suspend fun createImprt(imprtReq : CreateImprtReq): Response<CreateImprtRes> =
        retrofit.create(ImprtApi::class.java).postImpromptu(imprtReq)

    override suspend fun postImprtImage(
        images: RequestBody,
        file: MultipartBody.Part,
        impromptuId: Int
    ): Response<BaseRes>
    = retrofit.create(ImprtApi::class.java).postImprtImage(impromptuId, file, images)

    override suspend fun getImprtDetail(id: Int): Response<ImprtDetailRes>
    = retrofit.create(ImprtApi::class.java).getImprtDetail(id)

    override suspend fun getImpromptu(
        areaId: Int?,
        days: List<Int>?,
        memberCountIndex: Int?,
        page: Int,
        scheduleIndex: Int?
    ): Response<GetImprtRes>
    = retrofit.create(ImprtApi::class.java).getImprts(
        areaId = areaId,
        days = days,
        memberCountIndex = memberCountIndex,
        page = page,
        scheduleIndex = scheduleIndex
    )

    override suspend fun changePinStatus(id: Int): Response<BaseResultRes>
    = retrofit.create(ImprtApi::class.java).changePinStatus(id)

    override suspend fun joinImprt(id: Int): Response<BaseResultRes>
    = retrofit.create(ImprtApi::class.java).participateImprt(id)

    override suspend fun getSearchImprt(
        title: String,
        page: Int
    ): Response<GetImprtRes>
    = retrofit.create(ImprtApi::class.java).getImprtSearch(title, page)

    override suspend fun getMyImprt(): Response<GetMyImprt>
    = retrofit.create(ImprtApi::class.java).getMyImpromptus()

    override suspend fun getImprtMembers(imprtId: Int): Response<GetImprtMembersRes>
    = retrofit.create(ImprtApi::class.java).getImprtMembers(imprtId)

    override suspend fun dismissMember(imprtId: Int, memberId: Int): Response<BaseRes>
    = retrofit.create(ImprtApi::class.java).dismissMember(imprtId, memberId)

    override suspend fun getImprtMemberDetail(
        imprtId: Int,
        memberId: Int
    ): Response<GetMemberDetailRes>
    = retrofit.create(ImprtApi::class.java).getImprtMemberDetail(imprtId, memberId)


}