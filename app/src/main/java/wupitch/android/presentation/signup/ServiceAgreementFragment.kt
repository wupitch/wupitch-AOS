package wupitch.android.presentation.signup

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentServiceAgreementBinding

class ServiceAgreementFragment
    : BaseFragment<FragmentServiceAgreementBinding>(
    FragmentServiceAgreementBinding::bind,
    R.layout.fragment_service_agreement
) {

    private var agreementSatisfiedNum = 0
    private var nonAllButtonClicked = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        setTitleText()
        setStatusBar(R.color.white)

    }

    fun testClick() {
        Toast.makeText(requireContext(), "left icon clicked!", Toast.LENGTH_SHORT).show()
    }

    val checkedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) ++agreementSatisfiedNum
            else --agreementSatisfiedNum
        }

    fun setAllButtonClickListener () {
        nonAllButtonClicked = false
        if (!binding.toggleAgreeAll.isChecked && !nonAllButtonClicked) {
            binding.btnNext.isActivated = false
            binding.toggleServiceAgreement.isChecked = false
            binding.togglePrivacy.isChecked = false
            binding.togglePushNotification.isChecked = false
        }else {
            binding.btnNext.isActivated = true
            binding.toggleServiceAgreement.isChecked = true
            binding.togglePrivacy.isChecked = true
            binding.togglePushNotification.isChecked = true
        }
        checkIfAgreementSatisfied()
    }

    fun setNonAllButtonClickListener () {
        nonAllButtonClicked = true
        checkIfAgreementSatisfied()
    }

    private fun checkIfAgreementSatisfied() {
        binding.btnNext.isActivated =
            (binding.toggleServiceAgreement.isChecked && binding.togglePrivacy.isChecked) || binding.toggleAgreeAll.isChecked
        binding.toggleAgreeAll.isChecked = agreementSatisfiedNum >= 3
    }

    private fun setTitleText() {
        binding.tvServiceAgreementTitle.text =
            Html.fromHtml(getString(R.string.service_agreement_title))
    }

    fun checkForNavigationToRegion(view: View) {
        if (view.isActivated) Navigation.findNavController(view)
            .navigate(R.id.action_serviceAgreementFragment_to_regionFragment)
    }
}