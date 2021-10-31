package wupitch.android.domain.use_case.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import wupitch.android.common.Resource
import wupitch.android.data.remote.KakaoLoginReq
import wupitch.android.data.remote.KakaoLoginRes
import wupitch.android.domain.repository.KakaoLoginRepository
import java.io.IOException
import javax.inject.Inject

class PostKakaoLoginUseCase @Inject constructor(
    val kakaoLoginRepository: KakaoLoginRepository
) {
    operator fun invoke(kakaoUserInfo : KakaoLoginReq): Flow<Resource<KakaoLoginRes>> = flow {

        emit(Resource.Loading<KakaoLoginRes>())
        val response = kakaoLoginRepository.postKakaoUserInfo(kakaoUserInfo)

        if(response.isSuccessful) {
            response.body()?.let { result ->
                if(result.isSuccess) emit(Resource.Success(result))
                else emit(Resource.Error<KakaoLoginRes>(null, "An unexpected error occured"))
            }
        }else {
            emit(Resource.Error<KakaoLoginRes>(null, "An unexpected error occured"))
        }

    }

}