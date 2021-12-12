package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.LoginApi
import wupitch.android.data.remote.response.LoginRes
import wupitch.android.domain.model.LoginReq
import wupitch.android.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : LoginRepository {

    override suspend fun login(loginReq: LoginReq): Response<LoginRes>
    = retrofit.create(LoginApi::class.java).postLogin(loginReq)
}