package com.ho.baselib.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ho.baselib.presentation.viewbinding.FragmentBinding
import com.ho.baselib.presentation.viewbinding.FragmentBindingDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * BaseFragment 是一个通用的 Fragment 基类，提供视图缓存和数据加载功能。
 *
 * @param VB ViewBinding类型
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment(),
    FragmentBinding<VB> by FragmentBindingDelegate() {

    // 缓存根视图
    private var rootView: View? = null
    // 是否缓存根视图
    open var cacheRootView = false
    // 标记数据是否已加载
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 视图创建时缓存或重新创建视图
        return if (rootView == null || !cacheRootView) {
            createViewWithBinding(inflater, container).also { rootView = it }
        } else {
            rootView?.parent?.let { (it as ViewGroup).removeView(rootView) }
            bindView(rootView!!)
            rootView
        }
    }

    override fun onResume() {
        super.onResume()
        // 当 Fragment 恢复时加载数据
        if (!isDataLoaded) {
            isDataLoaded = true
            loadData()
        }
    }

    /**
     * 子类可以重写此方法以实现数据加载逻辑。
     */
    open fun loadData() {
        // 默认实现为空，子类需要重写此方法
    }

    /**
     * 启动一个协程来执行给定的代码块
     *
     * @param block 代码块
     */
    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch(block = block)
    }
}