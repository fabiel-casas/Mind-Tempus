package virtus.synergy.mindtempus.notifications


import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import virtus.synergy.core.notifications
import virtus.synergy.journal.Constants.JOURNAL_CHANNEL_ID
import virtus.synergy.journal.Constants.JOURNAL_NOTIFICATION_ID
import virtus.synergy.journal.Constants.JOURNAL_REQUEST_ACTION
import virtus.synergy.design_system.R
import virtus.synergy.mindtempus.navigation.NavDestination
import java.time.ZonedDateTime

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("****", "Notification intent: ${intent?.action}-${ZonedDateTime.now()}")
        if (intent?.action == JOURNAL_REQUEST_ACTION) {
            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {

        val url = "https://fabielcasas.nl/${NavDestination.EmotionalSelector.route}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, JOURNAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mind_tempus_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.notifications()
        manager.notify(JOURNAL_NOTIFICATION_ID, notification)
    }
}
