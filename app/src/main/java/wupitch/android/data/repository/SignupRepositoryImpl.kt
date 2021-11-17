package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.SignupApi
import wupitch.android.data.remote.dto.SignupRes
import wupitch.android.domain.model.SignupReq
import wupitch.android.domain.repository.SignupRepository
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : SignupRepository {

    override suspend fun signup(signupReq: SignupReq): Response<SignupRes>
    = retrofit.create(SignupApi::class.java).postSignup(signupReq)

}