package wupitch.android.presentation.ui.main.notification

import android.text.BoringLayout
import wupitch.android.domain.model.NotificationItem

data class NotificationState(
    val isLoading : Boolean = false,
    val data : List<NotificationItem> = emptyList(),
    val error : String = ""
)