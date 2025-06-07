package com.ho.baselib.presentation.dialog

import android.os.Bundle
import android.view.View
import com.ho.baselib.R
import com.ho.baselib.databinding.BaseDialogLoadingBinding

class LoadingDialog : BaseDialog<BaseDialogLoadingBinding>() {

    // 使用伴生对象创建 LoadingDialog 的实例
    companion object {
        private const val ARG_MESSAGE = "message"
        fun newInstance(message: String? = null): LoadingDialog {
            val dialog = LoadingDialog()
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
            dialog.arguments = args
            return dialog
        }
    }

    private var message: String? = null

    override var cacheRootView: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 从参数中获取消息
        message = arguments?.getString(ARG_MESSAGE) ?: getString(R.string.base_loading)
    }


    // 重新设置消息的方法
    fun setMessage(message: String?) {
        if (isAdded && !isDetached) {
            this.message = message
            binding.loadingText.text = message
        } else {
            // 如果当前对话框还没被添加到视图中或者已经被移除，
            // 只更新内部的消息变量，等对话框被显示时再更新视图。
            this.message = message
        }
    }


    override fun initViews(view: View, savedInstanceState: Bundle?) {
        super.initViews(view, savedInstanceState)
        binding.loadingText.text = message
    }


}