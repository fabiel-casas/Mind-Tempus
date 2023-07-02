package virtus.synergy.journal.screens.journal.selector

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import virtus.synergy.core.resultCatching
import virtus.synergy.journal.Constants
import virtus.synergy.journal.usecases.JournalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * Created on 10/05/2023.
 */
class EmotionsSelectorViewModel(
    private val journalUseCase: JournalUseCase
) : ViewModel() {

    private val emotionLevel = mutableStateOf(0f)
    private val emotionStatus = mutableStateOf("")
    private val emojis = mutableStateOf<List<String>>(emptyList())
    val state = EmotionsSelectorState(
        emotionalLevel = emotionLevel,
        emotionalStatus = emotionStatus,
        emojis = emojis,
    )
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    fun updateEmotionLevel(level: Float) {
        emotionLevel.value = level
        emojis.value = Constants.clusterOfEmotionsMap[level.toInt()] ?: emptyList()
    }

    fun onEmojiSelected(emoji: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resultCatching {
                journalUseCase.createEmotionEntry(
                    emotionLevel = emotionLevel.value.toInt(),
                    emoji = emoji
                )
            }.onSuccess {
                _navigationEvent.value = it
            }
        }
    }
}