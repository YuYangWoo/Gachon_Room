package com.cookandroid.gachon_study_room.ui.main.view.dialog

import android.content.Context
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.DialogConfirmBinding
import com.cookandroid.gachon_study_room.ui.base.BaseDialog

class ConfirmDialog(context: Context, private val title: String, private val content: String) :
    BaseDialog<DialogConfirmBinding>(context, R.layout.dialog_confirm) {
    override fun init() {
        super.init()
        binding.txtTitle.text = title
        binding.txtContent.text = content
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}
