package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface KakaoLoginApi {

    @POST("app/account/kakao")
    suspend fun postKakaoLogin(
        @Body kakaoUserInfo : KakaoLoginReq
    ) : Response<KakaoLoginRes>

}