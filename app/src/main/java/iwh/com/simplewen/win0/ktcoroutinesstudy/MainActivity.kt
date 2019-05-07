package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import iwh.com.simplewen.win0.ktcoroutinesstudy.Adapter.MyRecycle

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header.*
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
    private lateinit var dia: android.app.AlertDialog
    private val ms = "这是第一个Kotlin协程Demo!"
    private val ms2 = "hello,world!"
    private val ms3 = "author:by-iwh 2019"
    private var currentColorMode = "sun"
    private lateinit var ani: ObjectAnimator
    private lateinit var flag1: Job
    private lateinit var flag2: Job
    //扩展函数，简化Toast
    private fun MainActivity.tos(str: String = "IWH") {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        this@MainActivity.orderEx()
        toolbar.subtitle = "kotlin练习demo"




        //检测shortCuts
        when (intent.action) {
            "IWH" -> tos("通过shortCuts进入：${intent.action}")
            "IWH2" -> tos("通过shortCuts2进入：${intent.action}")
            "IWH3" -> tos("通过动态创建的shortCuts进入:${intent.action}")
        }
        //动态创建shortCUts
        getSystemService(ShortcutManager::class.java).apply {
            val s1 = ShortcutInfo.Builder(this@MainActivity, "short_3")
                .setShortLabel("动态创建的shortCuts 3")
                .setLongLabel("启动协程3")
                .setIcon(Icon.createWithResource(this@MainActivity, R.drawable.ic_launcher_one))
                .setDisabledMessage("暂时不可以使用！")
                .setIntent(Intent(
                    this@MainActivity, iwh.com.simplewen.win0.ktcoroutinesstudy.MainActivity::class.java
                )
                    .apply { action = "IWH3" })
                .build()
            this.dynamicShortcuts = Arrays.asList(s1)
        }
        //设置Recycle列表
        val myRecycle = recycle
        val myRecycleHor = recycle_hor
        val myRecycleStagger = recycle_stagger
        val sym = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        val lym = LinearLayoutManager(this)
        val hym = LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }
        //竖向Recycle
        myRecycle.apply {
            this.layoutManager = lym
            this.adapter = MyRecycle(20)

        }
        //横向Recycle
        myRecycleHor.apply {
            this.layoutManager = hym
            this.adapter = MyRecycle(10, 1)
        }
        //瀑布流
        myRecycleStagger.apply {
            this.layoutManager = sym
            this.adapter = MyRecycle(max = 10)
        }

        //设置侧滑栏
        val toggle = ActionBarDrawerToggle(this@MainActivity, draw, toolbar, R.string.app_name, R.string.app_name)
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
                Snackbar.make(toolbar, "开始协程", Snackbar.LENGTH_LONG).setAction("取消") {
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
        dia = android.app.AlertDialog.Builder(this@MainActivity).setTitle("开启Io协程")
            .setView(ProgressBar(this@MainActivity)).create()
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
                tos("协程已经取消！")
                swipe.isRefreshing = false
                setFab(1)
            } else {
                tos("重新启动协程！")
                initCoroutines()

            }
        }

    }

    /**
     * 初始化协程，并发改变文本内容
     */
    private fun initCoroutines() = launch {
        fabAnimation(true)
        Log.d("@@开始执行f1","-----")
        val f1 = async {
            Log.d("@@开始执行f1内部","-----")
            changeTv()
        }
        Log.d("@@开始执行f2","-----")
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
        tos("完成1")
        swipe.isRefreshing = false
        fabAnimation()


    }

    private fun changeTv2() = launch {
        ktCor2.text = ""
        repeat(ms2.length) {
            delay(600)
            ktCor2.text = "${ktCor2.text}${ms2[it]}"
        }
        tos("完成2！")
        setFab(1)
        fabAnimation()

    }

    //工具栏菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //导航菜单
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return when (p0.itemId) {
            R.id.nav_1 -> {
                startActivity(Intent(this@MainActivity, DesignDemo::class.java))
                //draw.closeDrawer(GravityCompat.START)
                true
            }
            R.id.nav_2 -> {
                startActivity(Intent(this@MainActivity, Main2Activity::class.java))
                true
            }
            else -> true
        }

    }

    override fun onBackPressed() {
        if (draw.isDrawerOpen(GravityCompat.START)) draw.closeDrawer(GravityCompat.START) else finish()
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
                            tos("t1${Thread.currentThread()} 子协程1启动")
                        }
                        //该线程在  Thread[Thread-9,5,main]
                        Log.d("@@thread2:", Thread.currentThread().toString())
                    }
                    //该线程在 thread[DefaultDispatcher-worker-1,5,main]
                    Log.d("@@thread3:", Thread.currentThread().toString())
                    //当前协程为Io协程，切换到主协程
                    withContext(Dispatchers.Main) {
                        delay(2000L)
                        tos("t2${Thread.currentThread()} IO协程执行完成！")
                        dia.dismiss()
                    }
                }
                tos("t3${Thread.currentThread()}Io协程 在 主协程启动")
                //当前协程 主协程：Thread[main,5,main]}
                Log.d("@@thread4:", Thread.currentThread().toString())
                true
            }
            R.id.colorSelect -> {
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
    private fun changeColor() {
        if (currentColorMode === "sun") {
            toolbar.setBackgroundColor(Color.BLACK)
            window.statusBarColor = Color.BLACK
            fab.setBackgroundColor(Color.BLACK)
            currentColorMode = "night"
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
            window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)
            fab.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
            currentColorMode = "sun"
        }
    }


    //依次执行
    private fun orderEx() = launch {
        launch {
            repeat(10) {
                //delay(500)
                Log.d("@@order1:", "=======$it=========")
            }
            launch {
                Log.d("@@order1 内部","===============")
            }

        }
        launch {
            Log.d("@@order2:", "================")
        }

    }




}
