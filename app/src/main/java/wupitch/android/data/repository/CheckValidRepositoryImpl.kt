package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CheckValidationApi
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.response.ValidationRes
import wupitch.android.domain.repository.CheckValidRepository
import javax.inject.Inject

class CheckValidRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : CheckValidRepository {

    override suspend fun checkNicknameValidation(validReq: NicknameValidReq): Response<ValidationRes>
    = retrofit.create(CheckValidationApi::class.java).checkNicknameValid(validReq)

    override suspend fun checkEmailValidation(validReq: EmailValidReq): Response<ValidationRes>
    = retrofit.create(CheckValidationApi::class.java).checkEmailValid(validReq)

}