package wupitch.android.data.repository

import retrofit2.Retrofit
import wupitch.android.WupitchApplication
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.data.remote.KakaoLoginApi
import wupitch.android.data.remote.KakaoLoginRes
import wupitch.android.domain.repository.KakaoLoginRepository
import javax.inject.Inject

class KakaoLoginRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : KakaoLoginRepository {

    override suspend fun postKakaoUserInfo(kakaoUserInfo: KakaoLoginReq) =
        retrofit.create(KakaoLoginApi::class.java).postKakaoLogin(kakaoUserInfo)


}