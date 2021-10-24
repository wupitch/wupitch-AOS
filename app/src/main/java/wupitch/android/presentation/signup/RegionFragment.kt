package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding

@AndroidEntryPoint
class RegionFragment
    : BaseFragment<FragmentRegionBinding>(FragmentRegionBinding::bind, R.layout.fragment_region) {

    private lateinit var regionBottomSheetFragment: RegionBottomSheetFragment
    val viewModel : SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this

        viewModel.userRegion.observe(viewLifecycleOwner, Observer {
            binding.btnSelectRegion.apply {
                text = it
                isActivated = true
            }
            binding.btnNext.isActivated = true
        })
    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }

    fun showRegionBottomSheet() {
        regionBottomSheetFragment = RegionBottomSheetFragment(viewModel)
        regionBottomSheetFragment.show(childFragmentManager, "region_bottom_sheet")
    }

    fun checkForNavigationToSport(view: View) {
        if (view.isActivated) Navigation.findNavController(view)
            .navigate(R.id.action_regionFragment_to_sportFragment)
    }
}