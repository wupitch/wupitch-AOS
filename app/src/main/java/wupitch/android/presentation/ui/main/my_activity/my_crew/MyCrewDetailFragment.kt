package wupitch.android.presentation.ui.main.my_activity.my_crew

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.accompanist.pager.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentMyCrewDetailBinding
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.my_activity.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.components.*
import wupitch.android.util.ReportType
import wupitch.android.util.changeTabFont

@AndroidEntryPoint
class MyCrewDetailFragment : BaseFragment<FragmentMyCrewDetailBinding>(
    FragmentMyCrewDetailBinding::bind,
    R.layout.fragment_my_crew_detail
) {

    private val viewModel: MyCrewViewModel by viewModels()
    private lateinit var reportDialog : ReportDialog
    private lateinit var pagerAdapter: MyCrewDetailVPAdapter
    private lateinit var reportBottomSheet : ReportBottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getInt("selectedTab")?.let { viewModel.selectedTab = it }
        arguments?.getInt("crewId")?.let { viewModel.crewId = it }
    }

    override fun onResume() {
        super.onResume()
        binding.viewpagerMycrewdetail.post {
            binding.viewpagerMycrewdetail.currentItem =  viewModel.selectedTab
        }
        binding.tablayoutMycrewdetail.setScrollPosition( viewModel.selectedTab, 0f, true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
        setToolbarButtons()
        viewModel.showReportDialog.observe(viewLifecycleOwner, Observer {
            if(it) reportDialog.show()
            else reportDialog.dismiss()
        })

    }

    private fun showReportBottomSheet() {
        reportBottomSheet = ReportBottomSheetFragment(viewModel, ReportType.CREW)
        reportBottomSheet.show(childFragmentManager, "report bottom sheet")
    }

    private fun setToolbarButtons() {
        reportDialog = ReportDialog(requireContext(), viewModel)
        binding.ivLeft.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivMore.setOnClickListener {
            showReportBottomSheet()
        }
    }

    private val tabLayoutOnPageChangeListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.position?.let {
                binding.tablayoutMycrewdetail.changeTabFont(it)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private fun setViewPager() {
        pagerAdapter = MyCrewDetailVPAdapter(this)
        binding.viewpagerMycrewdetail.adapter = pagerAdapter

        TabLayoutMediator(
            binding.tablayoutMycrewdetail,
            binding.viewpagerMycrewdetail
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.intro)
                1 -> getString(R.string.board)
//                2 -> getString(R.string.gallery)
                else -> getString(R.string.members)
            }
        }.attach()

        binding.tablayoutMycrewdetail.apply {
            changeTabFont(0)
            addOnTabSelectedListener(tabLayoutOnPageChangeListener)
        }
    }
}
