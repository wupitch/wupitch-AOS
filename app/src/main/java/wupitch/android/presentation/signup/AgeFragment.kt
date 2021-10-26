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
import wupitch.android.databinding.FragmentAgeBinding
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding
import wupitch.android.databinding.FragmentSportBinding

class AgeFragment
    : BaseFragment<FragmentAgeBinding>(FragmentAgeBinding::bind, R.layout.fragment_age) {

    private val viewModel: SignupViewModel by viewModels()
    private var checkedAge = -1



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        binding.radiogroupAge.setOnCheckedChangeListener { group, checkedId ->
            checkedAge = group.findViewById<RadioButton>(checkedId).tag.toString().toInt()
            binding.btnNext.isActivated = true
        }



    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }


    fun checkForNavigationToProfile(view: View) {
        if (view.isActivated) {
            Navigation.findNavController(view)
                .navigate(R.id.action_ageFragment_to_profileFragment)
            viewModel.setUserAge(checkedAge)
        }
    }


}