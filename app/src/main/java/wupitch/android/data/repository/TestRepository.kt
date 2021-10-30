package wupitch.android.data.repository

import retrofit2.create
import wupitch.android.WupitchApplication

class TestRepository {

    suspend fun postKakaoUserInfo(userInfo : KakaoLoginReq)=
        WupitchApplication.retrofit.create(TestApi::class.java).postTest(userInfo)
}