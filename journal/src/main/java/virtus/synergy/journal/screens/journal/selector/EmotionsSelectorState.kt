package virtus.synergy.journal.screens.journal.selector

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 *
 * Created on 10/05/2023.
 */
data class EmotionsSelectorState(
    val emotionalLevel: State<Float> = mutableStateOf(0f),
    val emotionalStatus: State<String> = mutableStateOf(""),
    val emojis: State<List<String>> = mutableStateOf(emptyList()),
)
