package com.ho.baselib.data.retrofit.exception

import java.net.SocketTimeoutException

/**
 * @Author hjw
 * @Date 2023/5/19 10:00
 * @Description
 */
class NetException(cause: Throwable?) : Throwable(cause),BaseHttpException {

    override val message: String
        get() = when (cause) {

            is SocketTimeoutException -> "网络连接超时,请检查网络后重试"

            else -> "网络异常,请检查网络后重试"
        }
}