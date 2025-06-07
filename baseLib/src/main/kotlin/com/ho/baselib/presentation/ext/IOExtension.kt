package com.ho.baselib.presentation.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author hjw
 * @Date 2022/3/9/17:03
 * @Description ioExecute
 */
suspend fun <T> ioExecute(func: suspend () -> T): T = withContext(Dispatchers.IO) {
    func.invoke()
}