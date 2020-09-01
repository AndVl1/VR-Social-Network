package ru.bmstu.iu9.vrsocialnetwork.fcm

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		Log.d(TAG, "From: ${remoteMessage.from}")
		remoteMessage.notification?.let {
			Log.d(TAG, "Message Notification Body: ${it.body}")
			val notification = NotificationCompat.Builder(this)
				.setContentTitle(remoteMessage.from)
				.setContentText(it.body)
				.build()
			val manager = NotificationManagerCompat.from(applicationContext)
			manager.notify(/*notification id*/0, notification)
		}
	}

	override fun onNewToken(token: String) {
		Log.d(TAG, token)
	}

	companion object {
		private const val TAG = "FCM SERVICE"
	}
}