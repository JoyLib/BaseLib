package com.example.baselib

import android.os.Bundle
import androidx.activity.viewModels
import com.example.baselib.databinding.ActivityMainBinding
import com.ho.baselib.presentation.activity.BaseActivity
import com.ho.baselib.presentation.ext.onClick

internal class MainActivity: BaseActivity<ActivityMainBinding>() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tv.onClick {
            viewModel.loadData()
        }
    }
}