package wupitch.android.presentation.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseActivity
import wupitch.android.databinding.ActivitySignupBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

//    private val viewModel : SignupViewModel by viewModels()
    //없어도 fragment 에서 activityViewModels() 사용할 수 있다. how?!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}