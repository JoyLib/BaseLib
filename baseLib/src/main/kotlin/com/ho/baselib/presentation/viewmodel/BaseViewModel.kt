package com.ho.baselib.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ho.baselib.data.retrofit.exception.BaseHttpException
import com.ho.baselib.presentation.ext.asLiveData
import com.ho.baselib.result.IResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    companion object {
        private const val TAG = "BaseViewModel"
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()


    private val _errMsg = MutableLiveData<String>()
    val errMsg = _errMsg.asLiveData()


    protected fun setLoading(isLoading: Boolean) {
        _loading.postValue(isLoading)
    }


    protected fun sendErrMessage(msg: String) {
        _errMsg.postValue(msg)
    }

    inline fun launch(
        scope: CoroutineScope = viewModelScope,//协程作用域
        exceptionHandler: CoroutineExceptionHandler? = defaultCoroutineExceptionHandler,
        crossinline block: suspend () -> Unit,
    ): Job {
        // 如果传入了异常处理器，则使用它创建指定的 CoroutineScope
        val coroutineScope = exceptionHandler?.let {
            CoroutineScope(scope.coroutineContext + it)
        } ?: scope
        return coroutineScope.launch {
            block.invoke()
        }
    }

    /**
     * 包装接口返回的数据便于流式调用
     * @param T
     * @param block
     * @receiver
     */
    inline fun <reified T> apiCall(block: () -> IResult<T>): T? {
        return when (val result = block.invoke()) {
            is IResult.Success -> result.value
            is IResult.Failure -> {
                throw result.throwable
            }
        }
    }

    /**
     * 默认的协程异常处理器
     */
    val defaultCoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "Caught an exception: $exception")
            when (exception) {
                is BaseHttpException -> sendErrMessage(exception.message ?: "任务失败")
                else -> sendErrMessage("任务失败")
            }
        }
    }
}
