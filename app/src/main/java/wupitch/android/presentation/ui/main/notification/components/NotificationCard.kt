package wupitch.android.presentation.ui.main.notification.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import wupitch.android.R
import wupitch.android.domain.model.NotificationItem
import wupitch.android.presentation.theme.Roboto

@Composable
fun NotificationCard(
    notificationCard: NotificationItem,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 20.dp)
            .clickable {
                Log.d("{NotificationCard}", "알림 clicked!")
                onClick()
            },
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = if (notificationCard.isSeen) painterResource(id = R.drawable.ic_bell_profile__1_)
            else painterResource(id = R.drawable.ic_bell_profile),
            contentDescription = "notification image"
        )

        Column(Modifier.padding(start = 16.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notificationCard.content,
                fontFamily = Roboto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if(notificationCard.isSeen) colorResource(id = R.color.black04)
                else colorResource(id = R.color.main_black)
            )
            Spacer(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth())
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = notificationCard.date,
                fontFamily = Roboto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if(notificationCard.isSeen) colorResource(id = R.color.gray06)
                        else colorResource(id = R.color.gray02)
            )
        }


    }

}