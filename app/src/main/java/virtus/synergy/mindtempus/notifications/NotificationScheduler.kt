package virtus.synergy.mindtempus.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import virtus.synergy.core.NotificationCreator
import virtus.synergy.core.alarmManager
import virtus.synergy.core.notifications
import virtus.synergy.journal.Constants.JOURNAL_CHANNEL_ID
import virtus.synergy.journal.Constants.JOURNAL_REQUEST_ACTION
import virtus.synergy.journal.Constants.JOURNAL_REQUEST_CODE
import virtus.synergy.journal.usecases.SettingUseCase
import virtus.synergy.design_system.R
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 *
 * Created on 08/05/2023.
 */
class NotificationScheduler(
    private val context: Context,
    private val settingUseCase: SettingUseCase,
) : NotificationCreator {

    override fun createNotification() {
        createChannel()
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            .apply {
                action = JOURNAL_REQUEST_ACTION
            }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            JOURNAL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.alarmManager()
        val notificationTime = settingUseCase.getNotificationTime()
        val scheduleTime = ZonedDateTime.of(
            LocalDate.now(),
            notificationTime.toLocalTime(),
            ZoneId.systemDefault()
        )
        val triggerTime = if (ZonedDateTime.now() > scheduleTime) {
            scheduleTime.plusDays(1L).toInstant().toEpochMilli()
        } else {
            scheduleTime.toInstant().toEpochMilli()
        }
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(triggerTime, pendingIntent),
            pendingIntent
        )
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            JOURNAL_CHANNEL_ID,
            context.getString(R.string.notification_channel_journal_reminder),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager: NotificationManager = context.notifications()
        notificationManager.createNotificationChannel(channel)
    }

    override fun cancel() {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            .apply {
                action = JOURNAL_REQUEST_ACTION
            }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getBroadcast(context, JOURNAL_REQUEST_CODE, intent, flags)
        val alarmManager = context.alarmManager()
        alarmManager.cancel(pendingIntent)
    }
}