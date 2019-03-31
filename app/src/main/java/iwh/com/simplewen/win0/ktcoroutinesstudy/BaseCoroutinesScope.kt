package iwh.com.simplewen.win0.ktcoroutinesstudy

import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * 该类实现协程上下文，也可以直接在Activity实现
 */
@ExperimentalCoroutinesApi
abstract class BaseCoroutinesScope:AppCompatActivity(),CoroutineScope by MainScope(){
    override fun onDestroy() {
        super.onDestroy()
        this@BaseCoroutinesScope.cancel()
    }
}