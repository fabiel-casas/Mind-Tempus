package virtus.synergy.journal.screens.journal.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/**
 *
 * Created on 01/05/2023.
 */
data class EmotionalListState(
    val journalRecordList: State<Map<String, List<JournalItemState>>> = mutableStateOf(emptyMap())
)

data class JournalItemState(
    val id: String,
    val emotion: String,
    val dayOfTheWeek: String,
    val monthYear: String,
    val time: String,
    val description: String,
)