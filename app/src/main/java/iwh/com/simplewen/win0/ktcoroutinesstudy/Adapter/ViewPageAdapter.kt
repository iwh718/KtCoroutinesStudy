package iwh.com.simplewen.win0.ktcoroutinesstudy.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter

/**
 * viewPageAdapter
 */
class ViewPageAdapter(fragmentManager: FragmentManager,private val fgList:ArrayList<Fragment>):FragmentPagerAdapter(fragmentManager){
    override fun getCount(): Int {
      return fgList.size
    }

    override fun getItem(p0: Int): Fragment {
        return fgList[p0]
    }



}