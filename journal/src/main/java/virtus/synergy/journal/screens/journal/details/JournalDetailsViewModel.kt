package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import virtus.synergy.core.resultCatching
import virtus.synergy.journal.usecases.JournalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
                journalUseCase.getOrCreateJournalEntryBy(journalId)
            }.onSuccess {
                this@JournalDetailsViewModel.journalId = journalId
                journalInfo.value = it
            }
        }
    }

    fun updateEmotionalDescription(index: Int, newParagraph: Paragraph) {
        journalInfo.apply {
            value = value.copy(
                paragraph = value.paragraph.map { paragraph ->
                    if (newParagraph.index == paragraph.index) newParagraph else paragraph
                }
            )
        }
    }

    fun updateJournalNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            journalUseCase.updateJournalNote(
                journalId = journalId,
                note = Gson().toJson(journalInfo.value.paragraph)
            )
        }
    }

    fun addNewRow(index: Int) {
        journalInfo.apply {
            value = value.copy(
                paragraph = value.paragraph.toMutableList().apply {
                    add(index+1, Paragraph(type = ParagraphType.BODY, data = ""))
                }
            )
        }
    }


}
