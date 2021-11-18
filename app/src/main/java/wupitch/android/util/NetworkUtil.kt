package wupitch.android.util

import okhttp3.ResponseBody
import retrofit2.Retrofit
import javax.inject.Inject


class NetworkUtil @Inject constructor(
    private val retrofit: Retrofit
) {
    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
        return retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(errorBody)
    }
}