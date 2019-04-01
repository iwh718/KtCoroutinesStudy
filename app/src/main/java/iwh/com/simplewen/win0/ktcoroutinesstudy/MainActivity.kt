package com.simplewen.win0.ktcoroutinesstudy
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.thread


/**
 * @author IWH冬
 * Coroutines 学习demo
 * demo 使用了一些Design库的组件
 */

//该注解去除IDE的实验性API提示
@ExperimentalCoroutinesApi
class MainActivity : BaseCoroutinesScope(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var dia: AlertDialog
    private val ms = "这是第一个Kotlin协程Demo!"
    private val ms2 = "hello,world!"
    private val ms3 = "author:by-iwh 2019"
    private var currentColorMode = "sun"
    private lateinit var ani: ObjectAnimator
    private lateinit var flag1: Job
    private lateinit var flag2: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "kotlin练习demo"

        //设置侧滑栏
        val toggle =   ActionBarDrawerToggle(this@MainActivity,draw,toolbar,R.string.app_name, R.string.app_name)
        draw.addDrawerListener(toggle)
        toggle.syncState()
        nav.setNavigationItemSelectedListener(this@MainActivity)

        //设置下拉刷新
        swipe.setColorSchemeResources(R.color.colorAccent)
        swipe.setOnRefreshListener {
            launch {
                delay(2000L)
                if (!(flag1.isActive && flag2.isActive)) {
                    initCoroutines()
                }
                Snackbar.make(toolbar,"开始协程",Snackbar.LENGTH_LONG).setAction("取消"){
                    flag1.cancel()
                    flag2.cancel()
                    swipe.isRefreshing = false
                    setFab(1)
                    fabAnimation()
                }.show()

            }
        }
        //处理预制数据
        ani = ObjectAnimator.ofFloat(fab, "rotation", 0f, 360f)
        dia = AlertDialog.Builder(this@MainActivity).setTitle("开启Io协程").setView(ProgressBar(this@MainActivity)).create()
        //开始协程
        initCoroutines()
        //设置动画
        fabAnimation(true)
        //添加监听器
        fab.setOnClickListener {
            if (flag1.isActive && flag2.isActive) {
                fabAnimation()
                flag1.cancel()
                flag2.cancel()
                Toast.makeText(this@MainActivity, "协程已经取消！", Toast.LENGTH_SHORT).show()
                swipe.isRefreshing = false
                setFab(1)
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
        fabAnimation(true)
        val f1 = async { changeTv() }
        val f2 = async { changeTv2() }
        setFab(0)
        flag1 = f1.await()
        flag2 = f2.await()


    }

    //通过协程更改UI线程的文本
    private fun changeTv() = launch {
        ktCor.text = ""
        toolbar.title = ""
        repeat(ms.length) {
            delay(500)
            ktCor.text = "${ktCor.text}${ms[it]}"
            toolbar.title = "${toolbar.title}${ms3[it]}"
        }
        Toast.makeText(this@MainActivity, "完成1", Toast.LENGTH_SHORT).show()
        swipe.isRefreshing = false
        fabAnimation()


    }

    private fun changeTv2() = launch {
        ktCor2.text = ""
        repeat(ms2.length) {
            delay(600)
            ktCor2.text = "${ktCor2.text}${ms2[it]}"
        }
        Toast.makeText(this@MainActivity, "完成2！", Toast.LENGTH_SHORT).show()
        setFab(1)
        fabAnimation()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
      return  when (p0.itemId) {
            R.id.nav_1 -> {
                Toast.makeText(this@MainActivity,"你点击：${p0.itemId}",Toast.LENGTH_SHORT).show()
                draw.closeDrawer(GravityCompat.START)
                return true
            }
          R.id.nav_2 ->{

              true
          }
            else -> true
        }

    }
    override fun onBackPressed() {
        if(draw.isDrawerOpen(GravityCompat.START)) draw.closeDrawer(GravityCompat.START) else finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                dia.show()
                //启动子协程：Io上下文
                launch(Dispatchers.IO) {
                    delay(3000L)
                    //启动新的系统线程
                    thread {
                        //启动子协程，上下文为 主协程，运行在主协程中
                        launch(Dispatchers.Main) {
                            delay(2000L)
                            //当前协程：Thread[main,5,main]}，可以访问UI组件
                            Log.d("@@thread1:", "${Thread.currentThread()} ")
                            Toast.makeText(this@MainActivity, "t1${Thread.currentThread()} 子协程1启动", Toast.LENGTH_SHORT)
                                .show()
                        }
                        //该线程在  Thread[Thread-9,5,main]
                        Log.d("@@thread2:", Thread.currentThread().toString())
                    }
                    //该线程在 thread[DefaultDispatcher-worker-1,5,main]
                    Log.d("@@thread3:", Thread.currentThread().toString())
                    //当前协程为Io协程，切换到主协程
                    withContext(Dispatchers.Main) {
                        delay(2000L)
                        Toast.makeText(this@MainActivity, "t2${Thread.currentThread()} IO协程执行完成！", Toast.LENGTH_SHORT)
                            .show()
                        dia.dismiss()
                    }
                }
                Toast.makeText(this@MainActivity, "t3${Thread.currentThread()}Io协程 在 主协程启动", Toast.LENGTH_SHORT).show()
                //当前协程 主协程：Thread[main,5,main]}
                Log.d("@@thread4:", Thread.currentThread().toString())
                true
            }
            R.id.colorSelect ->{
                changeColor()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //设置fab图标
    private fun setFab(status: Int) {
        when (status) {
            1 -> fab.setImageDrawable(getDrawable(R.drawable.ic_fab_play))
            0 -> fab.setImageDrawable(getDrawable(R.drawable.ic_fab_cancel))
        }
    }

    //设置fab属性动画
    private fun fabAnimation(status: Boolean = false) {
        when (status) {
            true -> {
                with(ani) {
                    duration = 1000
                    repeatCount = -1
                    interpolator = LinearInterpolator()
                    start()
                }
            }
            false -> {
                fab.rotation = 0f
                ani.cancel()
            }
        }

    }

    //更改主色调
    private fun changeColor(){
       if(currentColorMode === "sun"){
           toolbar.setBackgroundColor(Color.BLACK)
           window.statusBarColor = Color.BLACK
           fab.setBackgroundColor(Color.BLACK)
           currentColorMode = "night"
       }else{
           toolbar.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.colorPrimary))
           window.statusBarColor = ContextCompat.getColor(this@MainActivity,R.color.colorPrimaryDark)
           fab.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.colorAccent))
           currentColorMode = "sun"
       }
    }
}
