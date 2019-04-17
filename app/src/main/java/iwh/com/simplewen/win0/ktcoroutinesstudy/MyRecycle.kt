package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * recycle示例
 */
class MyRecycle(private val max:Int,private val type:Int = 0): RecyclerView.Adapter<MyRecycle.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv = view.findViewById<TextView>(R.id.text)
    }
    override fun onBindViewHolder(parent: MyViewHolder, position: Int) {
      parent.tv.text = "我是第 $position 个"

    }
    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): MyViewHolder {

        return  when(type){
           0-> MyViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.my_recycle, p0, false)).apply {
               this.tv.setOnClickListener{
                   Toast.makeText(p0.context,"点击：${this.adapterPosition} 个！",Toast.LENGTH_SHORT).show()
               }

           }
            else -> MyViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.my_recycle_2, p0, false)).apply{
                this.tv.setOnClickListener{
                    Toast.makeText(p0.context,"点击：${this.adapterPosition} 个！",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return max
    }
}