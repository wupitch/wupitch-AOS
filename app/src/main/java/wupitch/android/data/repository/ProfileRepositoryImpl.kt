package wupitch.android.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import wupitch.android.data.remote.ProfileApi
import wupitch.android.data.remote.dto.BaseRes
import wupitch.android.data.remote.dto.ChangePwReq
import wupitch.android.data.remote.dto.UpdateUserInfoReq
import wupitch.android.data.remote.dto.UserInfoRes
import wupitch.android.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val retrofit : Retrofit
) : ProfileRepository{

    override suspend fun getUserInfo(): Response<UserInfoRes>
    = retrofit.create(ProfileApi::class.java).getUserInfo()

    override suspend fun changePw(newPw: ChangePwReq): Response<BaseRes>
    = retrofit.create(ProfileApi::class.java).patchPw(newPw)

    override suspend fun changeNotiStatus(): Response<BaseRes>
    = retrofit.create(ProfileApi::class.java).patchNotiStatus()

    override suspend fun unregisterUser(): Response<BaseRes>
    = retrofit.create(ProfileApi::class.java).patchUnregister()

    override suspend fun postProfileImage(
        images: RequestBody,
        file: MultipartBody.Part
    ): Response<BaseRes> =retrofit.create(ProfileApi::class.java).postProfileImage(file, images)

    override suspend fun updateUserInfo(userInfoReq: UpdateUserInfoReq): Response<BaseRes>
    = retrofit.create(ProfileApi::class.java).updateUserInfo(userInfoReq)


}