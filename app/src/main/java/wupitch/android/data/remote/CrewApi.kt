package wupitch.android.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import wupitch.android.data.remote.dto.*
import wupitch.android.data.remote.response.*
import wupitch.android.domain.model.CreateCrewReq

interface CrewApi {
    @POST("app/clubs")
    suspend fun postCrew(
        @Body createCrewReq : CreateCrewReq
    ) : Response<CreateCrewRes>

    @Multipart
    @PATCH("app/clubs/image")
    suspend fun postCrewImage(
        @Query ("crewId") crewId: Int,
        @Part file : MultipartBody.Part,
        @Part("images") images: RequestBody
    ) : Response<BaseRes>

    @GET("app/clubs/{clubId}")
    suspend fun getCrewDetail(
        @Path("clubId") crewId : Int
    ) : Response<CrewDetailRes>

    @GET("app/clubs")
    suspend fun getCrews(
        @Query("ageList") ageList : List<Int>?,
        @Query("areaId") areaId : Int?,
        @Query("days") days : List<Int>?,
        @Query("memberCountValue") memberCountValue : Int?,
        @Query("page") page : Int,
        @Query("sportsList") sportsList : List<Int>?,
    ) : Response<GetCrewRes>

    @PATCH("app/clubs/{clubId}/pinUp-toggle")
    suspend fun changePinStatus(
        @Path("clubId") crewId : Int
    ): Response<BaseResultRes>

    @POST("app/clubs/{clubId}/participation-toggle")
    suspend fun participateCrew(
        @Path("clubId") crewId : Int
    ): Response<BaseResultRes>

    @GET("app/clubs/title")
    suspend fun getCrewSearch(
        @Query("crewTitle") crewTitle : String,
        @Query("page") page : Int,
        ) : Response<GetCrewRes>

    @GET("app/clubs/{clubId}/guest-info")
    suspend fun getCrewVisitorDates(
        @Path("clubId") crewId : Int
    ) : Response<GetCrewVisitorDates>

    @POST("app/clubs/guest-info")
    suspend fun postVisit(
        @Body visitReq : CrewVisitorReq
    ) : Response<BaseRes>

    @GET("app/clubs/accounts/auth")
    suspend fun getMyCrews() : Response<GetMyCrewRes>

    @POST("app/posts/crew/{crewId}")
    suspend fun createCrewPost(
        @Path("crewId") crewId : Int,
        @Body crewPostReq : CrewPostReq
    ): Response<BaseRes>

    @GET("app/posts/crew/{crewId}")
    suspend fun getCrewPosts(
        @Path("crewId") crewId : Int
    ) : Response<GetCrewPostRes>

    @PATCH("app/posts/{postId}/like-toggle")
    suspend fun patchPostLike(
        @Path("postId") postId : Int
    ) : Response<BaseResultRes>

    @PATCH("app/posts/{postId}/report-toggle")
    suspend fun patchPostReport(
        @Path("postId") postId : Int,
        @Body reportPostReq : ReportPostReq
    ) : Response<BaseResultRes>

    @GET("app/clubs/{clubId}/members")
    suspend fun getCrewMembers(
        @Path("clubId") crewId : Int
    ) : Response<GetCrewMembersRes>

}