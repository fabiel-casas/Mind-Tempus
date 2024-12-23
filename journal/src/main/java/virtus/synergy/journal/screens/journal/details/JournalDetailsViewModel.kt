package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import virtus.synergy.core.resultCatching
import virtus.synergy.journal.usecases.JournalUseCase

class JournalDetailsViewModel(
    private val journalUseCase: JournalUseCase,
) : ViewModel() {

    private val journalInfo = mutableStateOf(JournalInfo())
    private val paragraphTools = mutableStateOf(journalTools)
    val state = JournalDetailsState(
        journalInfo = journalInfo,
        paragraphTools = paragraphTools,
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

    fun onParagraphContentUpdated(newParagraph: Paragraph, cursorSelection: TextRange) {
        journalInfo.apply {
            value = value.copy(
                paragraph = value.paragraph.map { paragraph ->
                    if (newParagraph.index == paragraph.index) newParagraph else paragraph
                }
            )
        }
    }

    fun onSaveJournalNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            journalUseCase.updateJournalNote(
                journalId = journalId,
                note = Gson().toJson(journalInfo.value.paragraph)
            )
        }
    }

    fun onAddNewRow(index: Int) {
        val newParagraphs = journalInfo.value.paragraph
            .map { it.copy(isFocused = false) }
            .toMutableList()
            .apply {
                add(
                    index + 1,
                    Paragraph(data = "", isFocused = true)
                )
            }
        journalInfo.apply {
            value = value.copy(
                paragraph = newParagraphs
            )
        }
    }

    fun onToolActionSelected(journalParagraphTools: JournalParagraphToolsState) {
        journalInfo.value.paragraph
            .firstOrNull { it.isFocused }
            ?.let { paragraph ->
                when (journalParagraphTools.type) {
                    JournalParagraphTools.Bold -> makeTextBold(paragraph)
                    JournalParagraphTools.Italic -> makeTextItalic(paragraph)
                    JournalParagraphTools.Title -> {
                        makeTextTitle(paragraph)
                    }
                }
                highlightToolsBaseOnParagraph(paragraph)
            }
    }

    private fun makeTextTitle(titleParagraph: Paragraph) {
        journalInfo.apply {
            value = value.copy(
                paragraph = value.paragraph.map { paragraph ->
                    if (paragraph.index == titleParagraph.index) {
                        paragraph.copy(isTitle = !paragraph.isTitle)
                    } else {
                        paragraph
                    }
                }
            )
        }
    }

    private fun makeTextItalic(paragraph: Paragraph) {
    }

    private fun makeTextBold(paragraph: Paragraph) {
    }

    fun onParagraphFocusChanged(
        newSelectedParagraph: Paragraph,
        cursorSelection: TextRange
    ) {
        highlightToolsBaseOnParagraph(newSelectedParagraph)
    }

    private fun highlightToolsBaseOnParagraph(newSelectedParagraph: Paragraph) {
        paragraphTools.value = paragraphTools.value.map { tool ->
            when (tool.type) {
                JournalParagraphTools.Bold -> tool.copy(isSelected = newSelectedParagraph.isBold)
                JournalParagraphTools.Italic -> tool.copy(isSelected = newSelectedParagraph.isItalic)
                JournalParagraphTools.Title -> tool.copy(isSelected = newSelectedParagraph.isTitle)
            }
        }
    }
}
