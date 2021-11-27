package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import wupitch.android.data.remote.dto.BaseRes

interface FcmApi {

    @POST("app/app/fcm/test")
    suspend fun postToken(
        @Query("contents") contents : String,
        @Query("targetToken") targetToken : String,
        @Query("title") title : String,
    ) : Response<BaseRes>

}