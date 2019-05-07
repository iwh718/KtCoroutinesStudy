package iwh.com.simplewen.win0.ktcoroutinesstudy.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import iwh.com.simplewen.win0.ktcoroutinesstudy.Adapter.MyRecycle
import iwh.com.simplewen.win0.ktcoroutinesstudy.R
import kotlinx.android.synthetic.main.recycle_left_fg.view.*

/**
 * Fragment
 */
class ViewPagesFragments:Fragment(){
    lateinit var coroutines:Context
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return when(arguments?.getInt("type",2)){
            1 -> {
                val ly = inflater.inflate(R.layout.recycle_left_fg,null)
                ly.fgLeftRecycleView.apply {
                    adapter = MyRecycle(10,0)
                    layoutManager = LinearLayoutManager(coroutines)
                }
                ly
            }
            2 -> {
                val ly = inflater.inflate(R.layout.recycle_left_fg,null)
                ly.fgLeftRecycleView.apply {
                    adapter = MyRecycle(14,1)
                    layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
                }
                ly
            }
            3 -> {
                //横向
               val ly = inflater.inflate(R.layout.recycle_left_fg,null)
                ly.fgLeftRecycleView.apply {
                    adapter = MyRecycle(20,1)
                    layoutManager = LinearLayoutManager(coroutines).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                }
                ly
            }
            else -> {
                val ly =  inflater.inflate(R.layout.recycle_left_fg,null)
                ly.fgLeftRecycleView.apply {
                    adapter = MyRecycle(10,1)
                    layoutManager = LinearLayoutManager(coroutines).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                }
                ly
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.coroutines = context as Context
    }
}
