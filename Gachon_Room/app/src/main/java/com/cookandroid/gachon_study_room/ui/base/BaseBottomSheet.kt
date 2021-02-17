package com.cookandroid.gachon_study_room.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheet<VB: ViewDataBinding>(private val layoutId: Int) : BottomSheetDialogFragment() {
    protected lateinit var binding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        init()
        return binding.root
    }

    protected open fun init() {

    }
    protected open fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

}