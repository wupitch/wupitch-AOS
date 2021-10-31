package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.data.remote.KakaoLoginRes

interface KakaoLoginRepository {

    suspend fun postKakaoUserInfo(kakaoUserInfo : KakaoLoginReq) : Response<KakaoLoginRes>
}