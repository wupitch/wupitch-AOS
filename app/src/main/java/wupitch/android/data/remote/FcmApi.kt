package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.FcmReq
import wupitch.android.data.remote.dto.FcmReqTest
import wupitch.android.data.remote.dto.NotiRes

interface FcmApi {

    //test purpose
    @POST("app/fcm/test")
    suspend fun postToken(
     @Body fcmReq : FcmReqTest
    ) : Response<BaseRes>

    @PATCH("app/device-token")
    suspend fun patchFcmToken(
        @Body fcmReq : FcmReq
    ) : Response<BaseRes>

    @GET("app/fcms/accounts/auth")
    suspend fun getNotifications() : Response<NotiRes>

    @PATCH("app/fcms/{fcmId}/view")
    suspend fun patchNotificationStatus(
        @Path("fcmId") fcmId : Int
    ) : Response<BaseRes>


}