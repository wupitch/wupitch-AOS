package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentUseOfPersonalInfoDetailBinding

class UseOfPersonalInfoDetailFragment
    : BaseFragment<FragmentUseOfPersonalInfoDetailBinding>(FragmentUseOfPersonalInfoDetailBinding::bind, R.layout.fragment_use_of_personal_info_detail)
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

    }
    fun backToServiceAgreement() {
        view?.findNavController()?.navigateUp()
    }
}