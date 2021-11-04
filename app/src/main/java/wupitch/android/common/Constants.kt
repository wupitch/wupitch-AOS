package wupitch.android.common

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val API_URL = " https://prod.wupitch.site/"
    const val PREFERENCES_NAME = "settings"
    val JWT_PREFERENCE_KEY = stringPreferencesKey("jwt_token")
    val USER_ID = intPreferencesKey("user_id")
}