package wupitch.android.presentation.ui.main.notification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wupitch.android.common.Resource
import wupitch.android.domain.model.NotificationItem

class NotificationViewModel : ViewModel() {

    private var _notificationState = mutableStateOf(NotificationState())
    val notificationState : State<NotificationState> = _notificationState


    init {
        getNotifications()
    }

    private fun getNotifications() = viewModelScope.launch {
        //todo
        _notificationState.value = NotificationState(isLoading = true)
        delay(2000L)
        val notiList = listOf<NotificationItem>(
            NotificationItem(false, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(true, "glglglglglglglglggggggggggggggggggggggggggggggggggggggg", "21.11.12"),
            NotificationItem(true, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(false, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(false, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(false, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(false, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12"),
            NotificationItem(true, "어서오세요하하하하하하하하하하하ㅏ하하하하하하하하하하하ㅏㅎ", "21.11.12")
        )
        _notificationState.value = NotificationState(data = notiList)
    }
}