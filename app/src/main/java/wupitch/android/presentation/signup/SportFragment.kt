package wupitch.android.presentation.signup

import android.os.Bundle
import android.view.View
import wupitch.android.R
import wupitch.android.common.BaseFragment
import wupitch.android.databinding.FragmentRegionBinding
import wupitch.android.databinding.FragmentServiceAgreementBinding
import wupitch.android.databinding.FragmentSportBinding

class SportFragment
    : BaseFragment<FragmentSportBinding>(FragmentSportBinding::bind, R.layout.fragment_sport) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}