package wupitch.android.data.repository

import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.CheckValidationApi
import wupitch.android.data.remote.dto.ValidReq
import wupitch.android.data.remote.dto.ValidationRes
import wupitch.android.domain.repository.CheckValidRepository
import javax.inject.Inject

class CheckValidRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : CheckValidRepository {

    override suspend fun checkValidation(validReq: ValidReq): Response<ValidationRes>
    = retrofit.create(CheckValidationApi::class.java).checkNicknameValid(validReq)

}