package wupitch.android.common

import android.content.Context
import android.net.Uri
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
    const val INTRO_MAX_LENGTH = 500
    const val SUPPLY_MAX_LENGTH = 100

    val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")
}