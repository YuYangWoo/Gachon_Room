package com.cookandroid.gachon_study_room.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cookandroid.gachon_study_room.R

abstract class BaseActivity<VB: ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        splash()
        super.onCreate(savedInstanceState)
        init()
    }

    protected open fun init() {
        initViewDataBinding()
    }

    protected open fun splash() {
      setTheme(R.style.Theme_Gachon_Study_Room)
    }

    protected open fun initViewDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }
}