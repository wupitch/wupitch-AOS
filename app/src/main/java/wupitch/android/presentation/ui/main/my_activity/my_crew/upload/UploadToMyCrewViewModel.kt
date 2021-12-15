package wupitch.android.presentation.ui.main.my_activity.my_crew.upload

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.CrewPostReq
import wupitch.android.domain.repository.CrewRepository
import javax.inject.Inject

@HiltViewModel
class UploadToMyCrewViewModel @Inject constructor(
    private val crewRepository: CrewRepository
): ViewModel() {

    private var _crewId : Int = -1

    fun setCrewId(id : Int) {
        _crewId = id
    }

    private var _uploadPostState = mutableStateOf(BaseState())
    val uploadPostState : State<BaseState> = _uploadPostState

    fun uploadPost(content : String, announceTitle : String?) = viewModelScope.launch {
        _uploadPostState.value = BaseState(isLoading = true)


        val response = crewRepository.createCrewPost(
            crewId = _crewId,
            crewPostReq = CrewPostReq(
                contents = content,
                isNotice = if(announceTitle == null) false else true,
                noticeTitle = if(announceTitle == null) null else announceTitle
            )
        )

        if(response.isSuccessful){
            response.body()?.let { res ->
                if(res.isSuccess) _uploadPostState.value = BaseState(isSuccess = true)
                else _uploadPostState.value = BaseState(error = res.message)
            }
        }else _uploadPostState.value = BaseState(error = "게시글 업로드에 실패했습니다.")

    }
}