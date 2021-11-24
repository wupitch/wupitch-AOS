package wupitch.android.domain.repository

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.domain.model.CreateCrewReq

interface CrewRepository {

    suspend fun createCrew(crewReq : CreateCrewReq) : Response<CreateCrewRes>
    suspend fun postCrewImage(images : MultipartBody.Part, crewId : Int) : Response<BaseRes>

}