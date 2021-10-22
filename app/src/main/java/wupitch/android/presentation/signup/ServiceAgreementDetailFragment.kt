package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        binding.toolbarServiceAgreementDetail.llLeftIcon.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }

//    fun backToServiceAgreement() {
//        view?.findNavController()?.navigateUp()
//        Toast.makeText(requireContext(), "back clicked", Toast.LENGTH_SHORT).show()
//    }
}