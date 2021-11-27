package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq

interface FcmApi {

    @POST("app/fcm/test")
    suspend fun postToken(
     @Body fcmReq : FcmReq
    ) : Response<BaseRes>

}