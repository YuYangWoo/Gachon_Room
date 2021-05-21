package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.content.Context
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.DialogConfirmBinding
import com.cookandroid.gachon_study_room.ui.base.BaseDialog

class ConfirmDialog(context: Context) :
    BaseDialog<DialogConfirmBinding>(context, R.layout.dialog_confirm) {
    override fun init() {
        super.init()
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
