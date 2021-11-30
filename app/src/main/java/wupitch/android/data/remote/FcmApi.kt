package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq
import wupitch.android.data.remote.dto.NotiRes

interface FcmApi {

    //test purpose

//    @POST("app/fcm/test")
//    suspend fun postToken(
//     @Body fcmReq : FcmReq
//    ) : Response<BaseRes>

    @PATCH("app/device-token")
    suspend fun patchFcmToken(
        @Body fcmReq : FcmReq
    ) : Response<BaseRes>

    @GET("app/fcms/accounts/auth")
    suspend fun getNotifications() : Response<NotiRes>


}