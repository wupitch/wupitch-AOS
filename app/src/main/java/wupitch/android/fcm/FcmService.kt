package wupitch.android.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wupitch.android.R
import wupitch.android.common.BaseState
import wupitch.android.data.remote.dto.FcmReq
import wupitch.android.domain.repository.FcmRepository
import wupitch.android.presentation.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_NAME = "우피치 알림"
        private const val CHANNEL_DESCRIPTION = "우피치 알림 채널"
        private const val CHANNEL_ID = "Wupitch_notification_channel"
    }

    @Inject
    lateinit var fcmRepository : FcmRepository
    private var registerTokenState = mutableStateOf(BaseState())

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        registerToken(p0)

    }
    private fun registerToken(token : String) = CoroutineScope(Dispatchers.IO).launch {
        val response = fcmRepository.patchFcmToken(FcmReq(token))
        if(response.isSuccessful){
            response.body()?.let {
                if(!it.isSuccess) registerTokenState.value = BaseState(error = it.message)
            }
        }else  registerTokenState.value = BaseState(error = "토큰 등록에 실패했습니다.")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        createNotificationChannel()

        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body
//        Log.d("{FcmService.onMessageReceived}", "$title $message")


        NotificationManagerCompat.from(this)
            .notify(0, createNotification(title, message))

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

        }
    }

    private fun createNotification(title : String?, message : String?): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("FCM", "FCM")
        }


        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setColor(ContextCompat.getColor(this, R.color.main_orange))
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }

}