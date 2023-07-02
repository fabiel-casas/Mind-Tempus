package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import virtus.synergy.core.resultCatching
import virtus.synergy.journal.usecases.JournalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalDetailsViewModel(
    private val journalUseCase: JournalUseCase,
) : ViewModel() {

    private val journalInfo = mutableStateOf(JournalInfo())
    val state = JournalDetailsState(
        journalInfo = journalInfo,
    )
    private var journalId: String = ""

    fun loadJournal(journalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resultCatching {
                journalUseCase.getJournalBy(journalId)
            }.onSuccess {
                this@JournalDetailsViewModel.journalId = journalId
                journalInfo.value = it
            }
        }
    }

    fun updateEmotionalDescription(newDescription: String) {
        journalInfo.apply {
            value = value.copy(note = newDescription)
        }
    }

    fun updateJournalNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            journalUseCase.updateJournalNote(
                journalId = journalId, journalInfo.value.note
            )
        }
    }


}
