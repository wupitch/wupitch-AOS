package wupitch.android.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.CreateCrewRes
import wupitch.android.data.remote.dto.CrewDetailRes
import wupitch.android.data.remote.dto.GetCrewRes
import wupitch.android.domain.model.CreateCrewReq

interface CrewRepository {

    suspend fun createCrew(crewReq: CreateCrewReq): Response<CreateCrewRes>
    suspend fun postCrewImage(
        images: RequestBody,
        file: MultipartBody.Part,
        crewId: Int
    ): Response<BaseRes>

    suspend fun getCrewDetail(crewId: Int): Response<CrewDetailRes>
    suspend fun getCrew(
        ageList: List<Int>?,
        areaId: Int?, days: List<Int>?, memberCountValue: Int?,
        page: Int, sportId: List<Int>?
    ): Response<GetCrewRes>
}