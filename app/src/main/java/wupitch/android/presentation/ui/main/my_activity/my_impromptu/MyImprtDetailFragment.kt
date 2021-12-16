package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentMyImprtDetailBinding
import wupitch.android.domain.model.ImprtDetailResult
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.theme.WupitchTheme
import wupitch.android.presentation.ui.components.*
import wupitch.android.presentation.ui.main.impromptu.components.RemainingDays
import wupitch.android.presentation.ui.main.my_activity.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.components.ReportDialog
import wupitch.android.presentation.ui.main.my_activity.my_crew.MyCrewDetailVPAdapter
import wupitch.android.util.ReportType
import wupitch.android.util.changeTabFont

@AndroidEntryPoint
class MyImprtDetailFragment : BaseFragment<FragmentMyImprtDetailBinding>(
    FragmentMyImprtDetailBinding::bind,
    R.layout.fragment_my_imprt_detail
) {

    private val viewModel : MyImpromptuViewModel by viewModels()
    private lateinit var reportDialog : ReportDialog
    private lateinit var pagerAdapter: MyImprtDetailVPAdapter
    private lateinit var reportBottomSheet : ReportBottomSheetFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("impromptuId")?.let { id ->
            viewModel.getImprtDetail(id)
        }
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
        reportBottomSheet = ReportBottomSheetFragment(viewModel, ReportType.IMPROMPTU)
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
                binding.tablayoutMyimprtdetail.changeTabFont(it)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    }

    private fun setViewPager() {
        pagerAdapter = MyImprtDetailVPAdapter(this)
        binding.viewpagerMyimprtdetail.adapter = pagerAdapter

        TabLayoutMediator(
            binding.tablayoutMyimprtdetail,
            binding.viewpagerMyimprtdetail
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.intro)
                else -> getString(R.string.impromptu_members)
            }
        }.attach()

        binding.tablayoutMyimprtdetail.apply {
            changeTabFont(0)
            addOnTabSelectedListener(tabLayoutOnPageChangeListener)
        }
    }
}