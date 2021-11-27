package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CrewApi
import wupitch.android.data.remote.ImprtApi
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.data.remote.dto.CreateImprtRes
import wupitch.android.data.remote.dto.ImprtDetailRes
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.CreateImprtReq
import wupitch.android.domain.repository.CrewRepository
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


}