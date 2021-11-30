package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq

interface FcmRepository {
    suspend fun patchFcmToken(fcmReq: FcmReq) : Response<BaseRes>
}