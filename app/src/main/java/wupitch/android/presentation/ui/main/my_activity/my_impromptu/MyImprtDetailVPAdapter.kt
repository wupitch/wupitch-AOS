package wupitch.android.presentation.ui.main.my_activity.my_impromptu

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wupitch.android.common.Constants.MY_CREW_DETAIL_PAGE_NUM
import wupitch.android.common.Constants.SEARCH_PAGE_NUM


class MyImprtDetailVPAdapter(fm : Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = SEARCH_PAGE_NUM

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyImprtInfoFragment()
            else -> MyImprtMembersFragment()
        }
    }
}