package virtus.synergy.journal.screens.settings


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import virtus.synergy.core.runCatch
import virtus.synergy.journal.usecases.SettingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import virtus.synergy.core.NotificationCreator
import java.time.ZonedDateTime

class SettingViewModel(
    private val settingUseCase: SettingUseCase,
    private val notificationCreator: NotificationCreator,
) : ViewModel() {

    private val isNotificationEnabled = mutableStateOf(false)
    private val settingsInfo = mutableStateOf(SettingsInfo())
    val state: SettingsState = SettingsState(
        isNotificationEnabled = isNotificationEnabled,
        settingsInfo = settingsInfo
    )

    fun loadSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatch {
                settingsInfo.value = settingUseCase.getSettings()
            }
        }
    }

    fun saveNotificationTime(selectedTime: ZonedDateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatch {
                settingUseCase.saveNotificationTime(selectedTime)
                loadSettings()
                notificationCreator.cancel()
                notificationCreator.createNotification()
            }
        }
    }

    fun saveEmojiTime(selectedTime: ZonedDateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatch {
                settingUseCase.saveEmojiTime(selectedTime)
                loadSettings()
            }
        }
    }
}

