package wupitch.android.presentation.ui.main.notification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import wupitch.android.domain.model.NotificationItem

@Composable
fun NotificationList(
    notificationList : List<NotificationItem>,
    onClick : (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            items = notificationList
        ){ index, item ->
            //todo get next page.
                NotificationCard(notificationCard = item) {
                    onClick(index)
                }
        }
    }
}