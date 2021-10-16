package wupitch.android

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wupitch.android.common.Constants.API_URL
import wupitch.android.common.Constants.PREFERENCES_NAME
import wupitch.android.common.JwtTokenInterceptor
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class WupitchApplication : Application() {


    companion object {
        lateinit var retrofit: Retrofit

        val Context.dataStore by preferencesDataStore(
            name = PREFERENCES_NAME
        )
    }



    override fun onCreate() {
        super.onCreate()

        initRetrofitInstance()

    }

    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(JwtTokenInterceptor(this))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}