package com.ho.baselib.data.retrofit

import com.ho.baselib.data.retrofit.exception.ApiException
import com.ho.baselib.data.retrofit.exception.NetException
import com.ho.baselib.data.retrofit.exception.ServerException
import com.ho.baselib.result.IResult

/**
 * @Author hjw
 * @Date 2022/11/1/11:38
 * @Description
 */

inline fun <T, R, E : ApiException> ApiResult<R>.asResult(
    e: (R) -> E,
    success: (R) -> IResult<T>
): IResult<T> {
    return when (this) {
        is ApiResult.Success<R> -> {
            when (data) {
                is BaseResponse -> {
                    (data as BaseResponse)
                        .let {
                            if (it.success()) {
                                success(data)
                            } else {
                                IResult.Failure(e.invoke(data))
                            }
                        }
                }
                else -> success(data)
            }
        }

        is ApiResult.Error -> {
            IResult.Failure(ServerException(code,message))
        }

        is ApiResult.Exception -> {
            IResult.Failure(NetException((throwable)))
        }
    }
}
