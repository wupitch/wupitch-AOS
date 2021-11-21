package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.SignupRes
import wupitch.android.domain.model.SignupReq

interface SignupRepository {
    suspend fun signup(signupReq : SignupReq) : Response<SignupRes>
}