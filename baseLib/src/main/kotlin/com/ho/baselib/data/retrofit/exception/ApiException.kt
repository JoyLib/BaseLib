package com.ho.baselib.data.retrofit.exception

/**
 * @Author hjw
 * @Date 2023/5/19 10:35
 * @Description
 */
abstract class ApiException(private val code: Int) : Throwable(),BaseHttpException {

}