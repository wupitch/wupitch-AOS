package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentProfileBinding
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding

class ProfileFragment
    : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::bind, R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}