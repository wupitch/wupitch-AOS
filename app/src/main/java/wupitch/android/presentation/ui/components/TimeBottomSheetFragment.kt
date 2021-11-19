package wupitch.android.presentation.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import wupitch.android.R
import wupitch.android.databinding.FragmentTimeBottomSheetBinding
import wupitch.android.presentation.ui.main.home.HomeViewModel
import wupitch.android.presentation.ui.main.impromptu.ImpromptuViewModel
import wupitch.android.util.TimeType
import javax.inject.Inject

@AndroidEntryPoint
class TimeBottomSheetFragment @Inject constructor(
    private val timeType : TimeType,
    private val viewModel : ViewModel
) :  BottomSheetDialogFragment() {

    private var  _binding : FragmentTimeBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            when(viewModel){
                is HomeViewModel -> {
                    when(timeType){
                        TimeType.START -> viewModel.setTimeFilter(TimeType.START, binding.timePicker.hour, binding.timePicker.minute)
                        TimeType.END -> viewModel.setTimeFilter(TimeType.END, binding.timePicker.hour, binding.timePicker.minute)
                    }
                }
                is ImpromptuViewModel -> {
                    when(timeType){
                        TimeType.START -> viewModel.setTimeFilter(TimeType.START, binding.timePicker.hour, binding.timePicker.minute)
                        TimeType.END -> viewModel.setTimeFilter(TimeType.END, binding.timePicker.hour, binding.timePicker.minute)
                    }
                }

            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}