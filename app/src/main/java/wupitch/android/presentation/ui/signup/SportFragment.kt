package wupitch.android.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentSportBinding

class SportFragment
    : BaseFragment<FragmentSportBinding>(FragmentSportBinding::bind, R.layout.fragment_sport), OnStopSignupClick {

    private var checkedToggleNum = 0
    private val viewModel: SignupViewModel by activityViewModels()
    private lateinit var talentBottomSheet : SportTalentBottomSheetFragment
    private lateinit var stopSignupDialog : StopSignupDialog
    private var clickedSport = -1

    override fun onResume() {
        super.onResume()
        Log.d("{SportFragment.onResume}", checkedToggleNum.toString())
        checkForNextBtnActivation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        binding.toggleEtc.setOnCheckedChangeListener(etcToggleCheckedListener)

        setEtcEtLengthCounter()

        viewModel.userSportTalent.observe(viewLifecycleOwner, Observer {
            if(it==null) {
                binding.root.findViewWithTag<ToggleButton>(clickedSport.toString()).isChecked = false
                checkedToggleNum--
            }
        })

    }

    fun navigateUp() {
        view?.findNavController()?.navigateUp()
    }

    private fun setEtcEtLengthCounter() {
        binding.tvEtcSportLength.text = getString(R.string.etc_sport_length, 0)

        binding.etEtcSport.addTextChangedListener {
            it?.let {
                binding.tvEtcSportLength.text = getString(R.string.etc_sport_length, it.length)
            }

        }
    }

    fun checkForNavigationToAge(view: View) {
        Log.d("{SportFragment.checkForNavigationToProfile}", checkedToggleNum.toString())
        if (view.isActivated) {
            Navigation.findNavController(view)
                .navigate(R.id.action_sportFragment_to_ageFragment)
            getEtcInput()
        }
    }

    val toggleClickedListener =
        View.OnClickListener { view ->
            if ((view as ToggleButton).isChecked) {
                checkedToggleNum++
                clickedSport = view.tag.toString().toInt()
                openTalentBottomSheet(clickedSport)
            }
            else checkedToggleNum--
            checkForNextBtnActivation()
        }

    private fun openTalentBottomSheet(viewTag : Int) {
        //실력선택하고 나서 선택했다는 표시 같은거 버튼에서 볼 수 있으면 좋겠는데...
        talentBottomSheet = SportTalentBottomSheetFragment(viewModel, viewTag)
        talentBottomSheet.show(childFragmentManager, "sport_talent_bottom_sheet")
    }

    val etcToggleClickedListener =
        View.OnClickListener { view ->
            if ((view as ToggleButton).isChecked) {
                checkedToggleNum++
            }
            else checkedToggleNum--
            binding.clEtcSportContainer.isVisible = (view as ToggleButton).isChecked
            checkForNextBtnActivation()
        }

    private val etcToggleCheckedListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        binding.clEtcSportContainer.isVisible = isChecked
    }

    private fun checkForNextBtnActivation() {
        Log.d("{SportFragment.checkForNextBtnActivation}", checkedToggleNum.toString())
        binding.btnNext.isActivated = checkedToggleNum >= 1
    }

    private fun getEtcInput() {
        //todo : get user sport!
        binding.etEtcSport.text?.let {
            if (it.isNotEmpty()) viewModel.setUserEtcSport(it.toString())
        }
    }
    fun showStopSignupDialog() {
        stopSignupDialog = StopSignupDialog(requireContext(), this)
        stopSignupDialog.show()
    }

    override fun onStopSignupClick() {
        stopSignupDialog.dismiss()
        activity?.finish()
    }

}