package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.View
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.*

class WelcomeFragment
    : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::bind, R.layout.fragment_welcome) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        setUserNickname()



    }

    private fun setUserNickname() {
        //todo : user nick name datastore 에서 가져오기
        binding.tvWelcomeTitle.text = getString(R.string.welcome_title, "유저 닉네임")
    }

    val navigateToHome = View.OnClickListener {
        //mainActivity 로 이동.
        showCustomToast("welcome!")
    }


}