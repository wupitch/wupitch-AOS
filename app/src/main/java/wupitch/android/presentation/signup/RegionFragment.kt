package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding

class RegionFragment
    : BaseFragment<FragmentRegionBinding>(FragmentRegionBinding::bind, R.layout.fragment_region) {

    private lateinit var regionBottomSheetFragment: RegionBottomSheetFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
    }

    fun showRegionBottomSheet() {
        regionBottomSheetFragment = RegionBottomSheetFragment()
        regionBottomSheetFragment.show(childFragmentManager, "region_bottom_sheet")
    }

    fun checkForNavigationToSport(view: View) {
        if (view.isActivated) Navigation.findNavController(view)
            .navigate(R.id.action_regionFragment_to_sportFragment)
    }
}