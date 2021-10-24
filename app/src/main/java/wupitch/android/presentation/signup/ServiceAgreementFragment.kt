package wupitch.android.presentation.signup

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.common.CustomToolbar
import wupitch.android.databinding.FragmentServiceAgreementBinding

class ServiceAgreementFragment
    : BaseFragment<FragmentServiceAgreementBinding>(
    FragmentServiceAgreementBinding::bind,
    R.layout.fragment_service_agreement
) {

    private var agreementSatisfiedNum = 0
    private var nonAllButtonClicked = true

    override fun onResume() {
        super.onResume()
        checkIfAgreementSatisfied()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        setTitleText()
        setStatusBar(R.color.white)

    }

    fun testClick() {
        //왜 on left icon click 이거는 작동하고 detail 에서는 작동 안 하지? toast 도 안 띄워지고
        //그냥 on left icon click 설정만 해도 터진다. 왜지?
        Toast.makeText(requireContext(), "left icon clicked!", Toast.LENGTH_SHORT).show()
    }

    val checkedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) ++agreementSatisfiedNum
            else --agreementSatisfiedNum
        }

    fun setAllButtonClickListener() {
        nonAllButtonClicked = false
        if (!binding.toggleAgreeAll.isChecked && !nonAllButtonClicked) {
            binding.btnNext.isActivated = false
            binding.toggleServiceAgreement.isChecked = false
            binding.togglePrivacy.isChecked = false
            binding.togglePushNotification.isChecked = false
        } else {
            binding.btnNext.isActivated = true
            binding.toggleServiceAgreement.isChecked = true
            binding.togglePrivacy.isChecked = true
            binding.togglePushNotification.isChecked = true
        }
        checkIfAgreementSatisfied()
    }

    fun setNonAllButtonClickListener() {
        nonAllButtonClicked = true
        checkIfAgreementSatisfied()
    }

    private fun checkIfAgreementSatisfied() {
        binding.toggleAgreeAll.isChecked = agreementSatisfiedNum >= 3
        binding.btnNext.isActivated =
            (binding.toggleServiceAgreement.isChecked && binding.togglePrivacy.isChecked) || binding.toggleAgreeAll.isChecked
    }

    private fun setTitleText() {
        binding.tvServiceAgreementTitle.text =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(getString(R.string.service_agreement_title), FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(getString(R.string.service_agreement_title))
    }

    fun checkForNavigationToRegion(view: View) {
        if (view.isActivated) Navigation.findNavController(view)
            .navigate(R.id.action_serviceAgreementFragment_to_regionFragment)
    }

    fun showServiceAgreementDetailFragment(view: View) {
        view.findNavController()
            .navigate(R.id.action_serviceAgreementFragment_to_serviceAgreementDetailFragment)
    }


    fun showUseOfPersonalInfoDetailFragment(view: View) {
        view.findNavController()
            .navigate(R.id.action_serviceAgreementFragment_to_useOfPersonalInfoDetailFragment)
    }
}