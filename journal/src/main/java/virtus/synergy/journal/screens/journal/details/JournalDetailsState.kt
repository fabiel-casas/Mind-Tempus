package virtus.synergy.journal.screens.journal.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import kotlinx.serialization.Serializable
import virtus.synergy.design_system.R
import java.util.UUID

data class JournalDetailsState(
    val journalInfo: State<JournalInfo> = mutableStateOf(JournalInfo()),
    val paragraphTools: State<List<JournalParagraphToolsState>> = mutableStateOf(emptyList()),
)

data class JournalInfo(
    val title: String = "",
    val paragraph: List<Paragraph> = emptyList(),
    val emoji: String = "",
    val emotionalIndex: Int? = null,
)

@Serializable
data class FormattedRange(
    val start: Int,
    val end: Int,
    val isBold: Boolean = false,
    val isItalic: Boolean = false
)

data class Paragraph(
    val index: String = UUID.randomUUID().toString(),
    val textFieldValue: TextFieldValue,
    val data: String,
    val isFocused: Boolean = false,
    val isTitle: Boolean = false,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val formattedRanges: List<FormattedRange> = emptyList()
)

fun Paragraph.newTextFieldValue(textRange: TextRange?): TextFieldValue {
    return TextFieldValue(
        annotatedString = buildAnnotatedString(),
        selection = textRange ?: TextRange(data.length),
    )
}

private fun Paragraph.buildAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        val originalText = data
        val ranges = formattedRanges.sortedBy { it.start }
        var currentIndex = 0

        for (range in ranges) {
            if (currentIndex < range.start) {
                append(originalText.substring(currentIndex, range.start))
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = if (range.isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (range.isItalic) FontStyle.Italic else FontStyle.Normal
                )
            ) {
                append(originalText.substring(range.start, range.end))
            }
            currentIndex = range.end
        }
        if (currentIndex < originalText.length) {
            append(originalText.substring(currentIndex))
        }
    }
}

sealed class JournalParagraphTools(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {
    object Title : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_title,
        title = R.string.tools_text_formating_title,
    )

    object Bold : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_bold,
        title = R.string.tools_text_formating_bold,
    )

    object Italic : JournalParagraphTools(
        icon = R.drawable.ic_text_formatting_italic,
        title = R.string.tools_text_formating_italic,
    )
}

data class JournalParagraphToolsState(
    val type: JournalParagraphTools,
    val isSelected: Boolean = false,
    val isVisible: Boolean = true,
)

val journalTools = listOf(
    JournalParagraphToolsState(JournalParagraphTools.Title),
    JournalParagraphToolsState(JournalParagraphTools.Bold),
    JournalParagraphToolsState(JournalParagraphTools.Italic),
)