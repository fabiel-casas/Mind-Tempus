package virtus.synergy.journal.screens.journal.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import virtus.synergy.core.resultCatching
import virtus.synergy.journal.extensions.addOrRemoveFormatting
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
    private var lastCursorPosition: TextRange? = null

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

    fun onParagraphTextChanged(newParagraph: Paragraph, textField: TextFieldValue) {
        if (textField.text == newParagraph.data && textField.selection == lastCursorPosition) return
        lastCursorPosition = textField.selection
        journalInfo.apply {
            value = value.copy(
                paragraph = value.paragraph.map { paragraph ->
                    if (newParagraph.index == paragraph.index) {
                        newParagraph.copy(
                            data = textField.text,
                            isFocused = true,
                            textFieldValue = textField
                        )
                    } else {
                        paragraph
                    }
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
                    Paragraph(data = "", isFocused = true, textFieldValue = TextFieldValue())
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
                val updatedParagraph = when (journalParagraphTools.type) {
                    JournalParagraphTools.Bold -> applyBoldToSelection(
                        paragraph,
                        lastCursorPosition
                    )

                    JournalParagraphTools.Italic -> applyItalicToSelection(
                        paragraph,
                        lastCursorPosition
                    )

                    JournalParagraphTools.Title -> makeTextTitle(paragraph)
                }
                journalInfo.value = journalInfo.value.copy(
                    paragraph = journalInfo.value.paragraph.map {
                        if (it.index == updatedParagraph.index) {
                            val newTextField =
                                updatedParagraph.newTextFieldValue(lastCursorPosition)
                            updatedParagraph.copy(
                                textFieldValue = newTextField
                            )
                        } else {
                            it
                        }
                    }
                )
                highlightToolsBaseOnParagraph(paragraph)
            }
    }

    private fun makeTextTitle(paragraph: Paragraph) = paragraph.copy(isTitle = !paragraph.isTitle)

    private fun applyBoldToSelection(paragraph: Paragraph, selection: TextRange?): Paragraph {
        if (selection == null) return paragraph
        val regex = Regex("\\*\\*(.*?)\\*\\*")

        val textUpdated = regex.addOrRemoveFormatting(paragraph, selection, "**")
        return paragraph.copy(data = textUpdated)
    }

    private fun applyItalicToSelection(paragraph: Paragraph, selection: TextRange?): Paragraph {
        if (selection == null) return paragraph
        val regex = Regex("\\*(.*?)\\*")
        val textUpdated = regex.addOrRemoveFormatting(paragraph, selection, "*")
        return paragraph.copy(data = textUpdated)
    }

    fun onParagraphFocusChanged(
        newSelectedParagraph: Paragraph,
        cursorSelection: TextRange
    ) {
        lastCursorPosition = cursorSelection
        changeParagraphFocus(newSelectedParagraph)
        highlightToolsBaseOnParagraph(newSelectedParagraph)
    }

    private fun changeParagraphFocus(newSelectedParagraph: Paragraph) {
        journalInfo.value = journalInfo.value.copy(
            paragraph = journalInfo.value.paragraph.map { paragraph ->
                paragraph.copy(isFocused = paragraph.index == newSelectedParagraph.index)
            }
        )
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
