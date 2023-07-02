package virtus.synergy.journal.usecases

import virtus.synergy.journal.screens.settings.SettingsInfo
import java.time.ZonedDateTime

interface SettingUseCase {

    suspend fun saveNotificationTime(hour: ZonedDateTime)

    suspend fun getSettings(): SettingsInfo

    suspend fun saveEmojiTime(hour: ZonedDateTime)
    fun getNotificationTime(): ZonedDateTime
    fun getEmojiTime(): ZonedDateTime
}