package wupitch.android.presentation.ui.main.my_activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import wupitch.android.databinding.DialogReportBinding

class ReportDialog(context: Context) : Dialog(context) {
    private lateinit var binding : DialogReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ivClose.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {
            //todo viewModel 에 보내기
            dismiss()
        }

    }
}