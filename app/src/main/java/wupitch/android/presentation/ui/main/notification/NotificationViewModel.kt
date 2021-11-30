package wupitch.android.presentation.ui.main.notification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.domain.model.NotificationItem
import wupitch.android.domain.repository.FcmRepository
import wupitch.android.util.dateDashToCol
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val fcmRepository: FcmRepository
) : ViewModel() {

    private var _notificationState = mutableStateOf(NotificationState())
    val notificationState : State<NotificationState> = _notificationState


    init {
        getNotifications()
    }

    private fun getNotifications() = viewModelScope.launch {
        _notificationState.value = NotificationState(isLoading = true)

        val response = fcmRepository.getNotifications()
        if(response.isSuccessful) {
            response.body()?.let { res ->
                if(res.isSuccess) {
                    _notificationState.value = NotificationState(data = res.result.map {
                        NotificationItem(
                            isSeen = it.isChecked,
                            content = it.contents,
                            date  = dateDashToCol(it.dateTime)
                        )
                    })
                }else _notificationState.value = NotificationState(error = res.message)
            }
        }else _notificationState.value = NotificationState(error = "알림 조회에 실패했습니다.")
    }
}