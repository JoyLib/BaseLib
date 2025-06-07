package com.ho.baselib.presentation.ext

/**
 * @Author hjw
 * @Date 2022/3/10/10:04
 * @Description 处理exception
 */
inline fun <reified T> call(
    func: () -> T
): Promise<T> {
    return try {
        val data = func()
        Promise(data, null)
    } catch (e: Throwable) {
        e.printStackTrace()
        Promise(null, e)
    }
}


class Promise<T>(var data: T?, private val error: Throwable? = null) {
    fun <K> then(func: (data: T) -> K): Promise<K> {
        if (error != null) {
            return Promise(null, error)
        }
        return try {
            if (data != null) Promise(func(data!!), null) else Promise(
                null,
                NullPointerException("data is null")
            )
        } catch (e: Exception) {
            Promise(null, e)
        }
    }


    fun catch(handlerError: ((e: Throwable) -> Unit)? = null) {
        error?.let {
            handlerError?.invoke(it)
        }
    }
}