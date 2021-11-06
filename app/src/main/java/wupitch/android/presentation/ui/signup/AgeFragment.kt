package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentAgeBinding
import wupitch.android.presentation.ui.MainViewModel

class AgeFragment
    : BaseFragment<FragmentAgeBinding>(FragmentAgeBinding::bind, R.layout.fragment_age), OnStopSignupClick {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var stopSignupDialog : StopSignupDialog

    override fun onResume() {
        super.onResume()
        observeUserAge()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        observeRadioBtnCheck()

    }

    private fun observeUserAge() {
        viewModel.userAge.observe(viewLifecycleOwner, Observer {
            binding.radiogroupAge.findViewWithTag<RadioButton>(it.toString()).performClick()
        })
    }

    private fun observeRadioBtnCheck() {
        binding.radiogroupAge.checkedRadioBtnTag.observe(viewLifecycleOwner, Observer {
            if (it != -1) binding.btnNext.isActivated = true
        })
    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }


    fun checkForNavigationToProfile(view: View) {
        if (view.isActivated) {
            Navigation.findNavController(view)
                .navigate(R.id.action_ageFragment_to_profileFragment)
            viewModel.setUserAge(binding.radiogroupAge.checkedRadioBtnTag.value!!)
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