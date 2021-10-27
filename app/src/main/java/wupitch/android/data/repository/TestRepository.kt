package wupitch.android.data.repository

import retrofit2.create
import wupitch.android.WupitchApplication

class TestRepository {

    suspend fun getBreakingNews()=
        WupitchApplication.retrofit.create(TestApi::class.java).getTest()
}