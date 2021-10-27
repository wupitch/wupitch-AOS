package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {
    @GET("oauth/authorize")
    suspend fun getTest(
        @Query("client_id") clientId : String = "9adf7b0734541aa03e830d55e6ac785b",
        @Query("redirect_uri") redirectUri : String = "https://prod.wupitch.site/app/account/kakao/callback",
        @Query("response_type") responseType : String = "code"
    ) : Response<TestResponse>


}