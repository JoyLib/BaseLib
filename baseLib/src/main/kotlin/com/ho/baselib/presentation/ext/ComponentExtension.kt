package com.ho.baselib.presentation.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ho.baselib.presentation.dialog.LoadingDialog
import com.ho.baselib.utils.LoadingDialogUtils

/**
 * @Author hjw
 * @Date 2025/1/16 14:37
 * @Description 组件扩展函数
 */

fun AppCompatActivity.showLoadingDialog(message: String? = null): LoadingDialog {
    return LoadingDialogUtils.showLoadingDialog(this, message)
}

fun AppCompatActivity.dismissLoadingDialog() {
    LoadingDialogUtils.dismissLoadingDialog()
}

fun Fragment.showLoadingDialog(message: String? = null): LoadingDialog {
    return LoadingDialogUtils.showLoadingDialog(this, message)
}

fun Fragment.dismissLoadingDialog() {
    LoadingDialogUtils.dismissLoadingDialog()
}
