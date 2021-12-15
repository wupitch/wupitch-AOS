package wupitch.android.common

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wupitch.android.CrewFilter
import com.wupitch.android.ImpromptuFilter

object Constants {
    const val API_URL = "https://dev.yogiyo-backend.shop/" //test
    //"https://prod.wupitch.site/" //real
    val Context.userInfoStore : DataStore<Preferences> by preferencesDataStore(
        name = "user_info"
    )
    val Context.crewFilterStore : DataStore<CrewFilter> by dataStore(
        fileName = "crew_filter.pb",
        serializer = CrewFilterSerializer
    )
    val Context.impromptuFilterStore : DataStore<ImpromptuFilter> by dataStore(
        fileName = "impromptu_filter.pb",
        serializer = ImpromptuFilterSerializer
    )
    val JWT_PREFERENCE_KEY = stringPreferencesKey("jwt_token")
    val USER_ID = intPreferencesKey("user_id")
    val FIRST_COMER = booleanPreferencesKey("is_first_comer")

    const val SEARCH_PAGE_NUM = 2
    const val MY_CREW_DETAIL_PAGE_NUM = 3
    const val INTRO_MAX_LENGTH = 500
    const val SUPPLY_MAX_LENGTH = 100
    const val ANNOUNCE_TITLE_LENGTH = 30

    val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")
    const val PAGE_SIZE = 10
}