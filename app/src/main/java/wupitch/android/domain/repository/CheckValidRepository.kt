package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.EmailValidReq
import wupitch.android.data.remote.dto.NicknameValidReq
import wupitch.android.data.remote.dto.ValidationRes

interface CheckValidRepository {
    suspend fun checkNicknameValidation(validReq : NicknameValidReq) : Response<ValidationRes>
    suspend fun checkEmailValidation(validReq : EmailValidReq) : Response<ValidationRes>
}