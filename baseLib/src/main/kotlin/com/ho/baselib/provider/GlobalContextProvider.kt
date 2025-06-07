package com.ho.baselib.provider

import android.content.Context

/**
 * 全局上下文提供者
 *
 */
object GlobalContextProvider {

    private var appContext: Context? = null

    // 初始化方法
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    // 获取全局上下文，未初始化时抛出异常
    fun getContext(): Context {
        return appContext
            ?: throw IllegalStateException("ContextProvider is not initialized. Call initialize() first.")
    }
}