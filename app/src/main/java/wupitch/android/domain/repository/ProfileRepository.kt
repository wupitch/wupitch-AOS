package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.ChangePwReq
import wupitch.android.data.remote.dto.UserInfoRes

interface ProfileRepository {
    suspend fun getUserInfo() : Response<UserInfoRes>
    suspend fun changePw(newPw : ChangePwReq) : Response<BaseRes>
    suspend fun changeNotiStatus() : Response<BaseRes>
    suspend fun unregisterUser() : Response<BaseRes>
}