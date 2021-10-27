package wupitch.android.presentation.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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