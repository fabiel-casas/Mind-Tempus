package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

data class JournalDetailsState(
    val journalInfo: State<JournalInfo> = mutableStateOf(JournalInfo()),
)

data class JournalInfo(
    val title: String = "",
    val note: String = "",
    val emoji: String = "",
    val emotionalIndex: Int = 0,
)


