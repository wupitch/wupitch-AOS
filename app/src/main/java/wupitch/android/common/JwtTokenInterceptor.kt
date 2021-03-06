package wupitch.android.common

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import wupitch.android.WupitchApplication.Companion.dataStore
import wupitch.android.common.Constants.JWT_PREFERENCE_KEY
import java.io.IOException

class JwtTokenInterceptor(
    @ApplicationContext val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()


        val jwtPreferenceFlow: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[JWT_PREFERENCE_KEY] ?: ""
            }

        val jwtToken: Flow<String> = jwtPreferenceFlow
        builder.addHeader("X-ACCESS-TOKEN", jwtToken.toString())
        return chain.proceed(builder.build())
    }
}