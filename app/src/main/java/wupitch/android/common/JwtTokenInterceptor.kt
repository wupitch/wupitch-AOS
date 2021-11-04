package wupitch.android.common

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class JwtTokenInterceptor(
    @ApplicationContext val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()


//        val jwtPreferenceFlow: Flow<String> = context.dataStore.data
//            .map { preferences ->
//                preferences[JWT_PREFERENCE_KEY] ?: ""
//            }
        //여기서 어떻게 data store 에서 jwt 가져오지 ?
        //어떤 coroutine scope 사용해야 되지???
        builder.addHeader("X-ACCESS-TOKEN", "")
        return chain.proceed(builder.build())
    }
}