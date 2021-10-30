package wupitch.android.presentation.ui.signup

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.common.BaseActivity
import wupitch.android.databinding.ActivitySignupBinding

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

//    private val viewModel : SignupViewModel by viewModels()
    //없어도 fragment 에서 activityViewModels() 사용할 수 있다. how?!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavBarColor()


    }

}