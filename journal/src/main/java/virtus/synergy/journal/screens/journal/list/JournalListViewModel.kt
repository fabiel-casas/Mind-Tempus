package virtus.synergy.journal.screens.journal.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import virtus.synergy.core.logError
import virtus.synergy.journal.usecases.JournalUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 *
 * Created on 01/05/2023.
 */
class JournalListViewModel(
    private val journalUseCase: JournalUseCase,
) : ViewModel() {

    private val journalItemList = mutableStateOf<Map<String, List<JournalItemState>>>(emptyMap())
    val state: EmotionalListState = EmotionalListState(
        journalRecordList = journalItemList
    )

    init {
        observeJournalList()
    }

    private fun observeJournalList() {
        journalUseCase.journalFLow()
            .catch { it.logError() }
            .onEach {
                journalItemList.value = it
            }
            .launchIn(viewModelScope)
    }
}