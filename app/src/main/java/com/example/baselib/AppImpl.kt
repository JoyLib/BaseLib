package com.example.baselib

import android.app.Application
import com.ho.baselib.utils.Logger
/**
 * @Author hjw
 * @Date 2025/1/16 15:22
 * @Description
 */
class AppImpl : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.d("app is launch ")
    }
}