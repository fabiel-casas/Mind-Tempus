package virtus.synergy.journal.screens.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.time.ZonedDateTime

data class SettingsState(
    val isNotificationEnabled: State<Boolean> = mutableStateOf(false),
    val settingsInfo: State<SettingsInfo> = mutableStateOf(SettingsInfo())
)

data class SettingsInfo(
    val notificationDisplayTime: String = "",
    val notificationTime: ZonedDateTime = ZonedDateTime.now(),
    val emojiDisplayTime: String = "",
    val emojiTime: ZonedDateTime = ZonedDateTime.now(),
)