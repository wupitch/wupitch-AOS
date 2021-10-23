package wupitch.android.presentation.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBottomSheetBinding

class RegionBottomSheetFragment
    : BottomSheetDialogFragment()
{

    private var _binding: FragmentRegionBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val regionList = arrayOf<String>(
        "서울시", "도봉구", "노원구", "강북구", "성북구", "은평구", "종로구", "동대문구",
        "중랑구", "서대문구", "중구", "성동구", "광진구", "마포구", "용산구", "강서구",
        "양천구", "구로구", "영등포구", "동작구", "관악구", "금천구", "서초구", "강남구",
        "송파구", "강동구"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegionBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomSheet = this
        setPicker()


    }

    private fun setPicker() {
        binding.pickerRegion.apply {
            minValue = 0
            maxValue = regionList.size -1
            binding.pickerRegion.displayedValues = regionList
        }
    }

    fun getPickerValue() {
        val pickedRegion = regionList[binding.pickerRegion.value]
        Log.d("{RegionBottomSheetFragment.getPickerValue}", pickedRegion)
        //viewmodel 에게 보내기.
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}