package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBinding

@AndroidEntryPoint
class RegionFragment
    : BaseFragment<FragmentRegionBinding>(FragmentRegionBinding::bind, R.layout.fragment_region), OnStopSignupClick {

    private lateinit var regionBottomSheetFragment: RegionBottomSheetFragment
    private lateinit var stopSignupDialog : StopSignupDialog
    val viewModel : SignupViewModel by activityViewModels()

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

    fun showStopSignupDialog() {
        stopSignupDialog = StopSignupDialog(requireContext(), this)
        stopSignupDialog.show()
    }

    override fun onStopSignupClick() {
        stopSignupDialog.dismiss()
        activity?.finish()
    }
}