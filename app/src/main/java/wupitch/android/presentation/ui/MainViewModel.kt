package wupitch.android.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.data.remote.dto.toSportResult
import wupitch.android.domain.model.SportResult
import wupitch.android.domain.repository.GetDistrictRepository
import wupitch.android.domain.repository.GetSportRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDistrictRepository : GetDistrictRepository,
    private val getSportRepository: GetSportRepository
) : ViewModel() {


}