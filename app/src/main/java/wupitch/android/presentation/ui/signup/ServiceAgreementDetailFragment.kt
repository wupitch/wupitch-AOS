package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentServiceAgreementDetailBinding

class ServiceAgreementDetailFragment
    : BaseFragment<FragmentServiceAgreementDetailBinding>(
    FragmentServiceAgreementDetailBinding::bind,
    R.layout.fragment_service_agreement_detail
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
    }

    fun backToServiceAgreement() {
        view?.findNavController()?.navigateUp()
    }
}