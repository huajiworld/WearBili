package cn.spacexc.wearbili.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.spacexc.wearbili.fragment.DynamicListFragment
import cn.spacexc.wearbili.fragment.ProfileFragment
import cn.spacexc.wearbili.fragment.RecommendVideoFragment

/**
 * Created by XC-Qan on 2022/6/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecommendVideoFragment()
            1 -> DynamicListFragment()
            2 -> ProfileFragment()
            //3 -> ProfileFragment()
            else -> ProfileFragment()
        }
    }
}