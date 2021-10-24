package wupitch.android.presentation.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding
import wupitch.android.databinding.FragmentSportBinding

class SportFragment
    : BaseFragment<FragmentSportBinding>(FragmentSportBinding::bind, R.layout.fragment_sport) {

    private var checkedToggleNum = 0
    private val viewModel: SignupViewModel by viewModels()
    private lateinit var talentBottomSheet : SportTalentBottomSheetFragment

    override fun onResume() {
        super.onResume()
        Log.d("{SportFragment.onResume}", checkedToggleNum.toString())
        checkForNextBtnActivation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragment = this
        binding.toggleBadminton.setOnClickListener(toggleClickedListener)
        binding.toggleSoccer.setOnClickListener(toggleClickedListener)
        binding.toggleVolleyball.setOnClickListener(toggleClickedListener)
        binding.toggleBasketball.setOnClickListener(toggleClickedListener)
        binding.toggleEtc.apply {
            setOnClickListener(etcToggleClickedListener)
            setOnCheckedChangeListener(etcToggleCheckedListener)
        }
        setEtcEtLengthCounter()

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

    fun checkForNavigationToProfile(view: View) {
        if (view.isActivated) {
            Navigation.findNavController(view)
                .navigate(R.id.action_sportFragment_to_profileFragment)
            getEtcInput()
        }
    }

    private val toggleClickedListener =
        View.OnClickListener { view ->
            if ((view as ToggleButton).isChecked) {
                checkedToggleNum++
                openTalentBottomSheet()
            }
            else checkedToggleNum--
            checkForNextBtnActivation()
        }

    private fun openTalentBottomSheet() {
        //실력선택하고 나서 선택했다는 표시 같은거 버튼에서 볼 수 있으면 좋겠는데...
        talentBottomSheet = SportTalentBottomSheetFragment(viewModel)
        talentBottomSheet.show(childFragmentManager, "sport_talent_bottom_sheet")
    }

    private val etcToggleClickedListener =
        View.OnClickListener { view ->
            if ((view as ToggleButton).isChecked) {
                checkedToggleNum++

                //edit text 보여줌과 동시에 나온다.
                //어떤 스포츠 종목인지 정하지도 않았는데 실력을 선택하라는 것이 조금 그러지 않나?
                //구체적인 스포츠를 입력하고 띄우도록 해야 될까?
                openTalentBottomSheet()
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

}