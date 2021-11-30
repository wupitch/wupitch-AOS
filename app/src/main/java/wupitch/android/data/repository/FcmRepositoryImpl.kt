package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.FcmApi
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq
import wupitch.android.domain.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : FcmRepository{
    override suspend fun patchFcmToken(fcmReq: FcmReq): Response<BaseRes>
    =  retrofit.create(FcmApi::class.java).patchFcmToken(fcmReq)
}