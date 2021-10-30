package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TestApi {
    @POST("app/account/kakao")
    suspend fun postTest(
        @Body kakaoUserInfo : KakaoLoginReq
    ) : Response<TestResponse>


}