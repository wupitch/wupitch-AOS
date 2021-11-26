package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.SignupRes
import wupitch.android.domain.model.SignupReq

interface SignupRepository {
    suspend fun signup(signupReq : SignupReq) : Response<SignupRes>
    suspend fun postIdCardImage(images: RequestBody, file : MultipartBody.Part) : Response<BaseRes>

}