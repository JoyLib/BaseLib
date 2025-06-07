package com.ho.baselib.utils

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ho.baselib.presentation.dialog.LoadingDialog

object LoadingDialogUtils {

    @SuppressLint("StaticFieldLeak")
    private var loadingDialog: LoadingDialog? = null

    fun showLoadingDialog(activity: AppCompatActivity, message: String? = null): LoadingDialog {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance(message)
            loadingDialog?.show(activity.supportFragmentManager, "LoadingDialog")
        } else {
            loadingDialog?.setMessage(message)
        }
        return loadingDialog!!
    }

    fun showLoadingDialog(fragment: Fragment, message: String? = null): LoadingDialog {
        dismissLoadingDialog() // 如果已有对话框，先关闭
        loadingDialog = LoadingDialog.newInstance(message)
        loadingDialog?.show(fragment.childFragmentManager, "LoadingDialog")
        return loadingDialog!!
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismissAllowingStateLoss()
        loadingDialog = null // 清空引用
    }
}