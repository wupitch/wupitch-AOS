package wupitch.android.presentation.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
), OnStopSignupClick {

    private var agreementSatisfiedNum = 0
    private var nonAllButtonClicked = true
    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var stopSignupDialog: StopSignupDialog
    private val viewModel : SignupViewModel by activityViewModels()

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(viewModel.userRegion.value != null) showStopSignupDialog()
//                else return 이때는 왜 아무것도 없지? todo : onboading activity 와 연결되었을 때 확인해볼것!!!
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    fun checkOkayToStopSignup() {
        if(viewModel.userRegion.value != null) showStopSignupDialog()
        else activity?.finish()
    }

    private fun showStopSignupDialog() {
        stopSignupDialog = StopSignupDialog(requireContext(), this)
        stopSignupDialog.show()
    }

    override fun onStopSignupClick() {
        stopSignupDialog.dismiss()
        activity?.finish()
    }

    override fun onDetach() {
        super.onDetach()
        backPressedCallback.remove()
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