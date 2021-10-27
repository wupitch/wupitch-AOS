package wupitch.android.presentation.signup

import android.os.Bundle
import android.text.Editable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_NULL
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentProfileBinding
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding

class ProfileFragment
    :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::bind, R.layout.fragment_profile),
    OnStopSignupClick {

    private lateinit var stopSignupDialog: StopSignupDialog
    private val viewModel: SignupViewModel by activityViewModels()
    private var jobForNickname: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        binding.tvIntroLength.text = getString(R.string.introduction_length, 0)

        binding.etProfileNickname.addTextChangedListener(nicknameTextWatcher)
        binding.etProfileIntro.addTextChangedListener(introTextWatcher)

        viewModel.isNicknameValid.observe(viewLifecycleOwner, Observer {
            Log.d("{ProfileFragment.onViewCreated}", it.toString())
            binding.tvNicknameAvailability.apply {
                this.text =
                    if (it) getString(R.string.available_nickname) else getString(R.string.not_available_nickname)
                isActivated = !it
                isVisible = binding.etProfileNickname.text.toString().isNotEmpty()
            }
        })
    }

    private val nicknameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s != null && s.isNotEmpty()) {
                jobForNickname?.cancel()
                jobForNickname = lifecycleScope.launch {
                    delay(1500L)
                    Log.d("{ProfileFragment.onTextChanged}", s.toString())
                    viewModel.checkNicknameValidation(s.toString())
                }
                binding.btnNext.isActivated = binding.etProfileIntro.text?.isNotEmpty() == true
            } else {
                binding.btnNext.isActivated = false
                binding.tvNicknameAvailability.isVisible = false
                viewModel.checkNicknameValidation(null)
            }

        }
        override fun afterTextChanged(s: Editable?) = Unit

    }

    private val introTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s != null && s.isNotEmpty()) {
                binding.tvIntroLength.text = getString(R.string.introduction_length, s.length)
                if (s.length > 100) {
                    Toast.makeText(requireContext(), "100자 이내로 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.btnNext.isActivated = binding.etProfileNickname.text?.isNotEmpty() == true
            } else {
                binding.btnNext.isActivated = false
            }
        }
        override fun afterTextChanged(s: Editable?) = Unit

    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }

    fun checkNavToWelcome(view: View) {
        if (view.isActivated) {
            viewModel.setUserIntroduction(binding.etProfileIntro.text.toString())
            view.findNavController().navigate(R.id.action_profileFragment_to_welcomeFragment)
        }
    }

    fun showStopSignupDialog() {
        stopSignupDialog = StopSignupDialog(requireContext(), this)
        stopSignupDialog.show()
    }

    override fun onStopSignupClick() {
        stopSignupDialog.dismiss()
        activity?.finish()
    }
}