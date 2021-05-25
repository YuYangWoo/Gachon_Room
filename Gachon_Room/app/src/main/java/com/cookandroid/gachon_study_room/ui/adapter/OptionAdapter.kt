package com.cookandroid.gachon_study_room.ui.adapter

import android.app.ProgressDialog.show
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.data.model.Option
import com.cookandroid.gachon_study_room.databinding.HolderOptionListBinding
import com.cookandroid.gachon_study_room.ui.main.view.dialog.ConfirmDialog

class OptionAdapter : RecyclerView.Adapter<OptionAdapter.ListViewHolder>() {
    var data: ArrayList<Option> = arrayListOf(Option("", "만든이"), Option("", "단과대 오픈채팅"), Option("", "개발자 건의사항"), Option("", "오픈소스 라이선스"), Option("","제휴문의"), Option("", "앱 버전 정보"))
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionAdapter.ListViewHolder {
        val binding = HolderOptionListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionAdapter.ListViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
     return data.size
    }

    inner class ListViewHolder(private val binding: HolderOptionListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(d: Option) {
            binding.list = d
        }
        init {
            binding.root.setOnClickListener {
                when(binding.txtOption.text.toString()) {
                    binding.root.resources.getString(R.string.developers) -> {
                        ConfirmDialog(binding.root.context, "만든이", "유양우, 강태종, 김재영, 구본의, 임한결").show()
                    }
                    binding.root.context.resources.getString(R.string.college_open_chatting) -> {

                    }
                    binding.root.context.resources.getString(R.string.suggestions) -> {

                    }
                    binding.root.context.resources.getString(R.string.opensource_license) -> {
                        ConfirmDialog(binding.root.context, "오픈소스 라이선스", "샬롸샬롸").show()
                    }
                    binding.root.context.resources.getString(R.string.partnership) -> {

                    }
                    binding.root.context.resources.getString(R.string.app_version) -> {

                    }
                }
            }
        }
    }
}