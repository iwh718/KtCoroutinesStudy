package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*


/**
 * @author IWH冬
 * Coroutines 学习demo
 */

//该注解去除IDE的实验性API提示
@ExperimentalCoroutinesApi
class MainActivity : BaseCoroutinesScope() {

    private val ms = "这是第一个协程Demo"
    private val ms2 = "author:iwh 2019"
    private val ms3 = "hello:world!"
    private lateinit var tv: TextView
    private lateinit var flag1: Job
    private lateinit var flag2: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initCoroutines()
        fab.setOnClickListener {
            if (flag1.isActive && flag2.isActive) {
                flag1.cancel()
                flag2.cancel()
                Toast.makeText(this@MainActivity, "协程已经取消！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "重新启动协程！", Toast.LENGTH_SHORT).show()
                initCoroutines()

            }
        }

    }

    /**
     * 初始化协程，并发改变文本内容
     */
    private fun initCoroutines() = launch {
        val f1 = async { changeTv() }
        val f2 = async { changeTv2() }

        flag1 = f1.await()
        flag2 = f2.await()

    }

    //通过协程更改UI线程的文本
    private fun changeTv() = launch {
        ktCor.text = ""
        toolbar.title =""
        repeat(ms.length) {
            delay(500)
            ktCor.text = "${ktCor.text}${ms[it]}"
            toolbar.title = "${toolbar.title}${ms3[it]}"
        }
        Toast.makeText(this@MainActivity, "完成1", Toast.LENGTH_SHORT).show()

    }

    private fun changeTv2() = launch {
        ktCor2.text = ""
        repeat(ms2.length) {
            delay(600)
            ktCor2.text = "${ktCor2.text}${ms2[it]}"
        }
        Toast.makeText(this@MainActivity, "完成2！", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
