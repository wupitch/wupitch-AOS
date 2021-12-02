package wupitch.android.presentation.ui.main.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import wupitch.android.common.Constants.SEARCH_PAGE_NUM


class SearchVPAdapter(fm : Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = SEARCH_PAGE_NUM

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SearchCrewFragment()
            else -> SearchImpromptuFragment()
        }
    }
}