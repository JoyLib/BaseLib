package com.ho.baselib.presentation.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ho.baselib.presentation.viewbinding.ActivityBinding
import com.ho.baselib.presentation.viewbinding.ActivityBindingDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @Author hjw
 * @Date 2023/5/18 14:00
 * @Description
 */
open class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    ActivityBinding<VB> by ActivityBindingDelegate() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithBinding()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1f) {
            resources
        }
    }


    override fun getResources(): Resources {
        val res = super.getResources()
        val configContext = createConfigurationContext(res.configuration)
        return configContext.resources.apply {
            configuration.fontScale = 1.0f
            displayMetrics.scaledDensity = displayMetrics.density * configuration.fontScale
        }
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) = lifecycleScope.launch(block = block)
}