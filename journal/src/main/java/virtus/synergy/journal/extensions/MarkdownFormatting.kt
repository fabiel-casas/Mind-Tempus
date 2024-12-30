package virtus.synergy.journal.extensions

import androidx.compose.ui.text.TextRange
import virtus.synergy.journal.screens.journal.details.Paragraph

fun Regex.addOrRemoveFormatting(
    paragraph: Paragraph,
    selection: TextRange,
    formatting: String = "**"
): String {
    val matchedBoldSelection = findAll(paragraph.data).map { match ->
        val start = match.range.first // Start index of the match
        val end = match.range.last // End index of the match
        val content = match.groupValues[1] // The content inside **
        Triple(content, start, end)
    }.firstOrNull {
        it.second >= selection.start && it.third <= selection.end
    }
    return if (matchedBoldSelection != null) {
        // Remove formatting
        paragraph.data.replaceRange(
            matchedBoldSelection.second - formatting.length,
            matchedBoldSelection.third + formatting.length,
            matchedBoldSelection.first
        )
    } else {
        // Add formatting
        val selectedText = paragraph.data.substring(selection.start, selection.end)
        paragraph.data.replaceRange(
            selection.start,
            selection.end,
            "$formatting$selectedText$formatting"
        )
    }
}