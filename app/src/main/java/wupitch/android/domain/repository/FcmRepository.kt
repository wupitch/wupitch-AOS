package wupitch.android.domain.repository

import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes

interface FcmRepository {
    suspend fun postToken(contents : String, token : String, title : String) : Response<BaseRes>
}