package wupitch.android.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.KakaoLoginRes
import wupitch.android.domain.model.KakaoLoginReq

interface KakaoLoginApi {

    @POST("app/accounts/kakao")
    suspend fun postKakaoLogin(
        @Body kakaoUserInfo : KakaoLoginReq
    ) : Response<KakaoLoginRes>

}