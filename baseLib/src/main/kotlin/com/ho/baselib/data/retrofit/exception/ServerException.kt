package com.ho.baselib.data.retrofit.exception

/**
 * @Author hjw
 * @Date 2023/5/19 10:12
 * @Description
 */

class ServerException(val code: Int, val _msg: String? = null) : Throwable("服务器内部错误,请联系客服"),
    BaseHttpException {
}