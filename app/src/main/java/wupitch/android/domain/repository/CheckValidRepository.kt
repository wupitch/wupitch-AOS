package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.ValidReq
import wupitch.android.data.remote.dto.ValidationRes

interface CheckValidRepository {
    suspend fun checkValidation(validReq : ValidReq) : Response<ValidationRes>
}