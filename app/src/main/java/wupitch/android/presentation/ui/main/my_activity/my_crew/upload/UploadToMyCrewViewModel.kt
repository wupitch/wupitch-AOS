package wupitch.android.presentation.ui.main.my_activity.my_crew.upload

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState


class UploadToMyCrewViewModel : ViewModel() {

    private var _uploadPostState = mutableStateOf(BaseState())
    val uploadPostState : State<BaseState> = _uploadPostState

    private var _crewId : Int = -1
    val crewId = _crewId

    fun setCrewId(id : Int) {
        _crewId = id
    }

    fun uploadPost()= viewModelScope.launch {
        _uploadPostState.value = BaseState(isLoading = true)
        delay(1200L)
        _uploadPostState.value = BaseState(isSuccess = true)

    }
}