package wupitch.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import wupitch.android.WupitchApplication.Companion.dataStore
import wupitch.android.common.BaseActivity
import wupitch.android.common.Constants.JWT_PREFERENCE_KEY
import wupitch.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("{MainActivity.onCreate}", keyHash.toString())

    }


}