package virtus.synergy.journal.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

fun markdownVisualTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val annotatedString = AnnotatedString.Builder()

        // Regex explanation:
        // **(.+?)**  - Matches bold text enclosed by ** (e.g., **bold**)
        // *(.+?)*    - Matches italic text enclosed by * (e.g., *italic*)
        // ~~(.+?)~~  - Matches strikethrough text enclosed by ~~ (e.g., ~~strike~~)
        // <sub>(.+?)</sub> - Matches subscript text enclosed by <sub> and </sub>
        // <sup>(.+?)</sup> - Matches superscript text enclosed by <sup> and </sup>
        val regex =
            Regex("\\*\\*(.+?)\\*\\*|\\*(.+?)\\*|~~(.+?)~~|\\<sub>(.+?)\\</sub>|\\<sup>(.+?)\\</sup>")
        val matches = regex.findAll(text.text)
        var currentIndex = 0

        for (match in matches) {
            if (currentIndex < match.range.first) {
                annotatedString.append(text.text.substring(currentIndex, match.range.first))
            }

            val bold = match.groups[1]?.value
            val italic = match.groups[2]?.value
            val strikethrough = match.groups[3]?.value
            val subscript = match.groups[4]?.value
            val superscript = match.groups[5]?.value

            when {
                bold != null -> annotatedString.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                italic != null -> annotatedString.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                strikethrough != null -> annotatedString.pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                subscript != null -> annotatedString.pushStyle(SpanStyle(fontSize = TextUnit.Unspecified))
                superscript != null -> annotatedString.pushStyle(SpanStyle(fontSize = TextUnit.Unspecified))
            }

            annotatedString.append(match.value)
            annotatedString.pop()

            currentIndex = match.range.last + 1
        }

        if (currentIndex < text.text.length) {
            annotatedString.append(text.text.substring(currentIndex))
        }

        TransformedText(
            text = annotatedString.toAnnotatedString(),
            offsetMapping = MarkDownOffsetMapping(text, annotatedString.toAnnotatedString())
        )
    }
}

internal class MarkDownOffsetMapping(
    private val originalText: AnnotatedString,
    private val transformedText: AnnotatedString
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = transformedText.text.length

    override fun transformedToOriginal(offset: Int): Int = originalText.text.length

}