package com.ho.baselib.presentation.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.ho.baselib.R
import com.ho.baselib.presentation.viewbinding.FragmentBinding
import com.ho.baselib.presentation.viewbinding.FragmentBindingDelegate
import java.lang.reflect.Field
import java.util.*

/**
 * Dialog基类
 */
abstract class BaseDialog<VB : ViewBinding> : DialogFragment(),
    FragmentBinding<VB> by FragmentBindingDelegate() {

    // 缓存根视图
    private var rootView: View? = null
    // 是否缓存根视图
    open var cacheRootView = false
    // 是否隐藏导航栏
    open var hidNavigationBar = false

    // 取消监听器
    private var dismissListener: DismissListener? = null

    // 是否从缓存获取视图
    protected var fromCache = false

    /**
     * onCreateView 创建视图
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null || !cacheRootView) {
            rootView = createViewWithBinding(inflater, container)
            fromCache = false
        } else {
            fromCache = true
            rootView?.parent?.let {
                (it as ViewGroup).removeView(rootView)
            }
            bindView(rootView!!)
        }
        return rootView
    }

    /**
     * onStart 设置对话框样式
     */
    override fun onStart() {
        super.onStart()
        if (dialog == null) {
            return
        }

        // 隐藏导航栏
        if (hidNavigationBar) {
            immersionBar {
                hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            }
        }

        dialog?.let {
            // 设置对话框背景透明
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 设置对话框宽高
            it.window?.attributes?.let { params ->
                params.gravity = gravity
                params.dimAmount = dimAmount
                params.width = windowWidth
                params.height = windowHeight
                it.window?.attributes = params
            }

            // 设置对话框取消状态
            it.setCanceledOnTouchOutside(canceledOnTouchOutside)
            it.setCancelable(cancelable)

            // 设置返回键监听
            it.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    !cancelable
                } else false
            }

            // 设置外部触摸事件
            if (outSideTouchable) {
                it.window?.let {
                    it.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    )
                    it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
            }
        }
    }

    /**
     * onViewCreated 初始化视图
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!fromCache) {
            initViews(view, savedInstanceState)
        }
    }

    /**
     * 初始化视图
     */
    open fun initViews(view: View, savedInstanceState: Bundle?) {

    }

    // 对话框外部触摸事件
    open val outSideTouchable = false

    // 对话框宽度缩放比例
    open val scaleWidth: Float
        get() = 0.8f

    // 对话框遮罩层透明度
    open val dimAmount: Float
        get() = 0.7f

    // 对话框位置
    open val gravity: Int
        get() = Gravity.CENTER

    // 对话框宽度
    open val windowWidth: Int
        get() {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            return (scaleWidth * dm.widthPixels).toInt()
        }

    // 对话框高度
    open val windowHeight: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    // 对话框是否可以取消
    open val canceledOnTouchOutside: Boolean
        get() = true

    // 对话框是否可以返回键取消
    open val cancelable: Boolean
        get() = true

    /**
     * DismissListener 取消监听器接口
     */
    interface DismissListener {
        fun onDismiss()
    }

    // 设置取消监听器
    fun setDismissListener(dismissListener: DismissListener?) {
        this.dismissListener = dismissListener
    }

    /**
     * onDismiss 对话框取消事件
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
    }

    /**
     * show 显示对话框
     */
    fun show(fragmentManager: FragmentManager) {
        showAllowingStateLoss(fragmentManager, UUID.randomUUID().toString())
    }

    /**
     * showAllowingStateLoss 允许状态丢失显示对话框
     */
    open fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)

            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}