package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.SignupApi
import wupitch.android.data.remote.response.BaseRes
import wupitch.android.data.remote.response.SignupRes
import wupitch.android.domain.model.SignupReq
import wupitch.android.domain.repository.SignupRepository
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit
) : SignupRepository {

    override suspend fun signup(signupReq: SignupReq): Response<SignupRes>
    = retrofit.create(SignupApi::class.java).postSignup(signupReq)

    override suspend fun postIdCardImage(
        images: RequestBody,
        file: MultipartBody.Part
    ): Response<BaseRes>  =retrofit.create(SignupApi::class.java).postIdCardImage(file, images)

}