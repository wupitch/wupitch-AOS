package wupitch.android.presentation.ui.main.my_activity.my_crew

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wupitch.android.common.Constants.MY_CREW_DETAIL_PAGE_NUM


class MyCrewDetailVPAdapter(fm : Fragment) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = MY_CREW_DETAIL_PAGE_NUM

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyCrewIntroFragment()
            1 -> MyCrewBoardFragment()
//            2 -> MyCrewGalleryFragment()
            else -> MyCrewMembersFragment()
        }
    }
}