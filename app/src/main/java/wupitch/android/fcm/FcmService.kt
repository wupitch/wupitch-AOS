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
    lateinit var  fcmRepository : FcmRepository

    private var registerTokenState = mutableStateOf(BaseState())

    private fun registerToken(token : String) = CoroutineScope(Dispatchers.IO).launch {
        val response = fcmRepository.postToken("테스트 내용", token, "테스트 제목")
        if(response.isSuccessful){
            response.body()?.let {
                if(!it.isSuccess) registerTokenState.value = BaseState(error = it.message)
            }
        }else  registerTokenState.value = BaseState(error = "토큰 등록에 실패했습니다.")
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("{FcmService.onNewToken}", p0.toString())

        registerToken(p0)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //fcm에서 message 를 수신할 때마다 호출된다.
        Log.d("{FcmService.onMessageReceived}", remoteMessage.toString())
        createNotificationChannel()

        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body

        Log.d("{FcmService.onMessageReceived}", "title : $title, message : $message")

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
            .setSmallIcon(R.drawable.logo2)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return notificationBuilder.build()
    }


    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("{FcmService.onCreate}", task.exception.toString())
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("{FcmService.onCreate}", token.toString())
            registerToken(token.toString())
        })
    }




}