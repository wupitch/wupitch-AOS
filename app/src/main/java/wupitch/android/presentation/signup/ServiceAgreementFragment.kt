package wupitch.android.presentation.signup

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentServiceAgreementBinding

class ServiceAgreementFragment
    : BaseFragment<FragmentServiceAgreementBinding>(FragmentServiceAgreementBinding::bind, R.layout.fragment_service_agreement) {

    private var agreementSatisfiedNum = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleText()

        binding.btnNext.setOnClickListener {
            if(it.isActivated) Navigation.findNavController(it).navigate(R.id.action_serviceAgreementFragment_to_regionFragment)
        }

        binding.toggleAgreeAll.setOnCheckedChangeListener { _, isChecked ->
               if(isChecked) {
                   agreementSatisfiedNum += 3
                   binding.btnNext.isActivated = true

                   binding.toggleServiceAgreement.isChecked = true
                   binding.togglePrivacy.isChecked = true
                   binding.togglePushNotification.isChecked = true
               } else {
                   agreementSatisfiedNum -= 3
                   binding.btnNext.isActivated = false

                   binding.toggleServiceAgreement.isChecked = false
                   binding.togglePrivacy.isChecked = false
                   binding.togglePushNotification.isChecked = false
               }
        }

        val checkedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    ++agreementSatisfiedNum
                }else {
                    --agreementSatisfiedNum
                }
                checkIfAgreementSatisfied()
            }

        binding.toggleServiceAgreement.setOnCheckedChangeListener(checkedChangeListener)
        binding.togglePrivacy.setOnCheckedChangeListener(checkedChangeListener)
        binding.togglePushNotification.setOnCheckedChangeListener(checkedChangeListener)



    }

    private fun checkIfAgreementSatisfied() {
         binding.btnNext.isActivated = agreementSatisfiedNum>=2
        binding.toggleAgreeAll.isChecked = agreementSatisfiedNum >=3
        Log.d("{ServiceAgreementFragment.checkIfAgreementSatisfied}", agreementSatisfiedNum.toString())
    }

    private fun setTitleText() {
        binding.tvServiceAgreementTitle.text = Html.fromHtml(getString(R.string.service_agreement_title))
    }
}