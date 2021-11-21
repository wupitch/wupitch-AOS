package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.LoginRes
import wupitch.android.domain.model.LoginReq

interface LoginRepository  {
    suspend fun login(loginReq : LoginReq) : Response<LoginRes>
}