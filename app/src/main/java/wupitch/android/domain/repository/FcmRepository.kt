package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq
import wupitch.android.data.remote.dto.FcmReqTest
import wupitch.android.data.remote.dto.NotiRes

interface FcmRepository {
    suspend fun postToken(fcmReq : FcmReqTest): Response<BaseRes>
    suspend fun patchFcmToken(fcmReq: FcmReq) : Response<BaseRes>
    suspend fun getNotifications() : Response<NotiRes>
}