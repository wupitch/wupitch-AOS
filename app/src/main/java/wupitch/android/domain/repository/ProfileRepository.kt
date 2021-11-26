package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.ChangePwReq

interface ProfileRepository {
    suspend fun changePw(newPw : ChangePwReq) : Response<BaseRes>
    suspend fun changeNotiStatus() : Response<BaseRes>
}