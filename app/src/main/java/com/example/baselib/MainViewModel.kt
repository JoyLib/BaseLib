package com.example.baselib

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ho.baselib.presentation.ext.ioExecute
import com.ho.baselib.presentation.viewmodel.BaseViewModel
import com.ho.baselib.result.IResult

/**
 * @Author hjw
 * @Date 2023/5/18 15:03
 * @Description
 */
internal class MainViewModel : BaseViewModel() {


    fun observer(
        lifecycleOwner: LifecycleOwner,
        loadingObserver: Observer<Boolean>,
        msgObserver: Observer<String>
    ) {

        loading.observe(lifecycleOwner, loadingObserver)

        errMsg.observe(lifecycleOwner, msgObserver)
    }

    fun loadData() {
        launch{
        }
    }


    private suspend fun a() {
        ioExecute {
        }
    }
}