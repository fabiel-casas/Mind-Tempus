package virtus.synergy.journal.usecases

import android.content.SharedPreferences
import virtus.synergy.core.toHourMinutes
import virtus.synergy.journal.Constants.EMOJI_TIME
import virtus.synergy.journal.Constants.NOTIFICATION_TIME
import virtus.synergy.journal.data.ZonedDateTimeDeserializer
import virtus.synergy.journal.screens.settings.SettingsInfo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class SettingUseCaseImpl(private val sharedPreferences: SharedPreferences) : SettingUseCase {

    private val deserializer = ZonedDateTimeDeserializer()
    override suspend fun saveNotificationTime(hour: ZonedDateTime) {
        val convertHour = deserializer.write(hour)
        with(sharedPreferences.edit()) {
            putString(NOTIFICATION_TIME, convertHour)
            apply()
        }
    }

    override suspend fun saveEmojiTime(hour: ZonedDateTime) {
        val convertHour = deserializer.write(hour)
        with(sharedPreferences.edit()) {
            putString(EMOJI_TIME, convertHour)
            apply()
        }
    }

    override suspend fun getSettings(): SettingsInfo {
        val notificationTime = getNotificationTime()
        val readEmojiTime = sharedPreferences
            .getString(EMOJI_TIME, null)
        val savedEmojiTime =
            deserializer.read(readEmojiTime) ?: ZonedDateTime.now(ZoneId.systemDefault())

        return SettingsInfo(
            notificationDisplayTime = notificationTime.toHourMinutes(),
            notificationTime = notificationTime,
            emojiDisplayTime = savedEmojiTime.toHourMinutes(),
            emojiTime = savedEmojiTime,
        )
    }

    override fun getNotificationTime(): ZonedDateTime {
        val readNotificationTime = sharedPreferences
            .getString(NOTIFICATION_TIME, null)
        val notificationTime = ZonedDateTime.of(
            LocalDate.now(),
            LocalTime.of(9, 0),
            ZoneId.systemDefault()
        )
        return deserializer.read(readNotificationTime) ?: notificationTime
    }

    override fun getEmojiTime(): ZonedDateTime {
        val readEmojiTime = sharedPreferences
            .getString(EMOJI_TIME, null)
        val savedTime = deserializer.read(readEmojiTime)
        val localTime = savedTime?.toLocalTime() ?: LocalTime.of(8, 0)
        return ZonedDateTime.of(
            LocalDate.now(),
            localTime,
            ZoneId.systemDefault()
        )
    }

}