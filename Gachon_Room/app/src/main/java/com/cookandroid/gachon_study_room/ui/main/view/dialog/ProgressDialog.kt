package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.DialogLoadingBinding
import com.cookandroid.gachon_study_room.ui.base.BaseDialog

class ProgressDialog(context: Context) : BaseDialog<DialogLoadingBinding>(context, R.layout.dialog_loading) {
    override fun init() {
        super.init()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}