package wupitch.android.common

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val API_URL = "https://kauth.kakao.com/"
    const val PREFERENCES_NAME = "settings"
    val JWT_PREFERENCE_KEY = stringPreferencesKey("jwt_token")
}