package wupitch.android.presentation.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.scopes.FragmentScoped
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBottomSheetBinding
import wupitch.android.databinding.FragmentSportTalentBottomSheetBinding
import javax.inject.Inject

@FragmentScoped
class SportTalentBottomSheetFragment @Inject constructor(
    val viewModel : SignupViewModel
)
    : BottomSheetDialogFragment()
{

    private var _binding: FragmentSportTalentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var checkedTalent = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportTalentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomSheet = this

        isCancelable = false
        //스포츠 선택했는데 실력 선택하지 않았을 경우는?

        binding.radiogroupSportTalent.setOnCheckedChangeListener { group, checkedId ->
            checkedTalent = group.findViewById<RadioButton>(checkedId).tag.toString().toInt()
            binding.btnDoneSportTalent.isActivated = true

            //todo : match each talent tag to given server code!!
        }

    }

    fun getTalentValue(view: View) {

        if(view.isActivated) {
            viewModel.getUserSportTalent(checkedTalent)
            dismiss()
        }else {
            Toast.makeText(requireContext(), "실력을 선택해주세요.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun checkIfTalentSelected() {
        if(checkedTalent == -1) {
            Toast.makeText(requireContext(), "실력을 선택해주세요.", Toast.LENGTH_SHORT)
                .show()
        }else {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}