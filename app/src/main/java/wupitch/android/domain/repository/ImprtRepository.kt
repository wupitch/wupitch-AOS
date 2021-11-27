package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.data.remote.dto.CreateImprtRes
import wupitch.android.data.remote.dto.ImprtDetailRes
import wupitch.android.domain.model.CreateCrewReq
import wupitch.android.domain.model.CreateImprtReq

interface ImprtRepository {

    suspend fun createImprt(imprtReq : CreateImprtReq) : Response<CreateImprtRes>
    suspend fun postImprtImage(images: RequestBody, file : MultipartBody.Part, impromptuId : Int) : Response<BaseRes>
    suspend fun getImprtDetail(id : Int) : Response<ImprtDetailRes>
}