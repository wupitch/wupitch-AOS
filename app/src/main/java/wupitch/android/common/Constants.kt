package wupitch.android.common

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object Constants {
    const val API_URL = "https://prod.wupitch.site/"
    const val PREFERENCES_NAME = "settings"
    val Context.dataStore by preferencesDataStore(
        name = PREFERENCES_NAME
    )
    val JWT_PREFERENCE_KEY = stringPreferencesKey("jwt_token")
    val USER_ID = intPreferencesKey("user_id")
    const val SEARCH_PAGE_NUM = 2
}