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
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::bind, R.layout.fragment_profile) {


    private val viewModel: SignupViewModel by viewModels()
    private var jobForNickname: Job? = null
    var jobForIntro: Job? = null

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

        viewModel.isProfileBtnActivated.observe(viewLifecycleOwner, Observer {
            Log.d("{ProfileFragment.onViewCreated}", it.toString())
            binding.btnNext.isActivated = it
        })
    }

    private val nicknameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if(it.isNotEmpty()){
                    jobForNickname?.cancel()
                    jobForNickname = lifecycleScope.launch {
                        delay(2000L)
                        Log.d("{ProfileFragment.onTextChanged}", it.toString())
                        viewModel.checkNicknameValidation(it.toString())
                    }
                }else {
                    binding.tvNicknameAvailability.isVisible = false
                    viewModel.checkNicknameValidation(null)
                }
            }
        }
        override fun afterTextChanged(s: Editable?) = Unit

    }

    private val introTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if(it.length >100){
                    Toast.makeText(requireContext(), "100자 이내로 작성해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.tvIntroLength.text = getString(R.string.introduction_length, it.length)
                if (it.isNotEmpty()) {
                    jobForIntro?.cancel()
                    jobForIntro = lifecycleScope.launch {
                        delay(2000L)
                        Log.d("{ProfileFragment.onTextChanged}", it.toString())
                        viewModel.setUserIntroduction(it.toString())
                    }
                }else {
                    viewModel.setUserIntroduction(null)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit

    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }
}