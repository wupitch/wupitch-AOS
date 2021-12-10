package wupitch.android.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import wupitch.android.common.Constants.userInfoStore
import java.io.IOException

class JwtTokenInterceptor(
    @ApplicationContext val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder: Request.Builder = chain.request().newBuilder()

        runBlocking {
            val jwtPreferenceFlow = context.userInfoStore.data.first()
            val token = jwtPreferenceFlow[Constants.JWT_PREFERENCE_KEY] ?: ""
            builder.addHeader("X-ACCESS-TOKEN", token)
        }

        return chain.proceed(builder.build())
    }
}