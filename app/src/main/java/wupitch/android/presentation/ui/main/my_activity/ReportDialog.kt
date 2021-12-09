package wupitch.android.presentation.ui.main.my_activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.lifecycle.ViewModel
import wupitch.android.databinding.DialogReportBinding
import wupitch.android.presentation.ui.main.my_activity.my_crew.MyCrewViewModel
import wupitch.android.presentation.ui.main.my_activity.my_impromptu.MyImpromptuViewModel

class ReportDialog(context: Context, val viewModel : ViewModel) : Dialog(context) {
    private lateinit var binding : DialogReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.ivClose.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {
            when(viewModel){
                is MyCrewViewModel -> {
                    viewModel.postCrewReport(binding.etReport.text.toString())
                }
                is MyImpromptuViewModel -> {
                    viewModel.postImprtReport(binding.etReport.text.toString())
                }
            }
            dismiss()
        }

    }
}