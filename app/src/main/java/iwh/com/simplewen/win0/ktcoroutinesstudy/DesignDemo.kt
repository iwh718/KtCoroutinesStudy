package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.design.widget.BottomSheetDialog
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_design_demo.*

class DesignDemo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_demo)
        fab3.setOnClickListener {
            BottomSheetDialog(this@DesignDemo).apply {
                setTitle("这是一个底部弹出Alert!")
                setContentView(LinearLayout.inflate(this@DesignDemo,R.layout.bottom_sheet_layout,null))
                create()
                show()

            }
        }
    }
    //底部栏，底部弹出栏

}
