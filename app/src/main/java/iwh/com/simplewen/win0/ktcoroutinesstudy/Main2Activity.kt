package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import iwh.com.simplewen.win0.ktcoroutinesstudy.Adapter.MyRecycle
import iwh.com.simplewen.win0.ktcoroutinesstudy.Adapter.ViewPageAdapter
import iwh.com.simplewen.win0.ktcoroutinesstudy.fragments.ViewPagesFragments
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recycle_stagger2.apply {
            this.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            this.adapter = MyRecycle(40)
        }

        toolbar2.title = "折叠栏标题"
        toolbar2.setTitleTextColor(Color.WHITE)
        toolbar2.subtitle = "2019"

        //tabLayout
        tabLayout2.apply {
            addTab(this.newTab().setText("左边"))
            addTab(this.newTab().setText("中间"))
            addTab(this.newTab().setText("右边"))
                this.setSelectedTabIndicatorColor(Color.WHITE)
            addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabReselected(p0: TabLayout.Tab?) = Unit

                override fun onTabSelected(p0: TabLayout.Tab?){
                    viewPage2.currentItem = p0!!.position
                    when(p0.position){
                        0 ->bottomBar2.selectedItemId = R.id.BottomBarLeft
                        1 ->bottomBar2.selectedItemId = R.id.BottomBarCenter
                        2 ->bottomBar2.selectedItemId = R.id.bottomBarRight
                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?)  = Unit
            })
        }
        //fragments列表
        val fgList = arrayListOf<Fragment>().apply {
           repeat(3){
               add(ViewPagesFragments().apply {
                   this.arguments = Bundle().apply {
                       putInt("type",it  + 1)
                   }
               })
           }
        }
        //viewPage
        viewPage2.apply {
            offscreenPageLimit = 2
            adapter = ViewPageAdapter(supportFragmentManager,fgList)
            addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) = Unit
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int)  = Unit

                override fun onPageSelected(p0: Int) {
                    tabLayout2.getTabAt(p0)?.select()
                    when(p0){
                        0 ->bottomBar2.selectedItemId = R.id.BottomBarLeft
                        1 ->bottomBar2.selectedItemId = R.id.BottomBarCenter
                        2 ->bottomBar2.selectedItemId = R.id.bottomBarRight
                    }
                }
            })
        }
        //底部栏
        bottomBar2.apply {
            this.setOnNavigationItemSelectedListener  {
                Snackbar.make(this,"show：${it.itemId}",Snackbar.LENGTH_SHORT).show()
             return@setOnNavigationItemSelectedListener   when(it.itemId){
                    R.id.BottomBarLeft -> {
                        tabLayout2.getTabAt(0)?.select()
                        viewPage2.currentItem = 0
                        true
                    }
                    R.id.BottomBarCenter ->{
                        tabLayout2.getTabAt(1)?.select()
                        viewPage2.currentItem = 1
                        true
                    }
                    R.id.bottomBarRight -> {
                        viewPage2.currentItem = 2
                        tabLayout2.getTabAt(2)?.select()
                        true
                    }
                 else -> true
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
