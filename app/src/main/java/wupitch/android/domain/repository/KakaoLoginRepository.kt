package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.domain.model.KakaoLoginReq
import wupitch.android.data.remote.dto.KakaoLoginRes

interface KakaoLoginRepository {

    suspend fun postKakaoUserInfo(kakaoUserInfo : KakaoLoginReq) : Response<KakaoLoginRes>
}